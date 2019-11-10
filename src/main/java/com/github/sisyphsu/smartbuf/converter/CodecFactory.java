package com.github.sisyphsu.smartbuf.converter;

import com.github.sisyphsu.smartbuf.converter.codec.*;
import com.github.sisyphsu.smartbuf.reflect.XType;
import com.github.sisyphsu.smartbuf.reflect.XTypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CodecFactory, meantains CodecMethod and Pipeline.
 * It should scan all Codec to generate ConverterMethod, and cache all related classes to build ConverterMap,
 * Then use the ConverterMap to find the shortest data-converter-path.
 *
 * @author sulin
 * @since 2019-05-20 16:14:54
 */
@Slf4j
public final class CodecFactory {

    public static final CodecFactory Instance = new CodecFactory();

    private final XTypeFactory                   xTypeFactory = new XTypeFactory();
    private final Set<Codec>                     codecs       = ConcurrentHashMap.newKeySet();
    private final ConverterMap                   converterMap = new ConverterMap();
    private final Map<Object, ConverterPipeline> pipelineMap  = new ConcurrentHashMap<>();

    /**
     * Initialize CodecFactory with the specified Codec type.
     */
    public CodecFactory() {
        this.installCodec(ArrayCodec.class);
        this.installCodec(AtomicCodec.class);
        this.installCodec(AwtCodec.class);
        this.installCodec(BufferCodec.class);
        this.installCodec(CollectionCodec.class);
        this.installCodec(IOCodec.class);
        this.installCodec(JavaxCodec.class);
        this.installCodec(JodaCodec.class);
        this.installCodec(LangCodec.class);
        this.installCodec(MapCodec.class);
        this.installCodec(MathCodec.class);
        this.installCodec(NetCodec.class);
        this.installCodec(NumberCodec.class);
        this.installCodec(PrimaryCodec.class);
        this.installCodec(ReferenceCodec.class);
        this.installCodec(SqlCodec.class);
        this.installCodec(StringCodec.class);
        this.installCodec(ThrowableCodec.class);
        this.installCodec(TimeCodec.class);
        this.installCodec(UtilCodec.class);
    }

    /**
     * Install new codec
     *
     * @param codecClass new codec's class
     */
    public void installCodec(Class<? extends Codec> codecClass) {
        try {
            this.installCodec(codecClass.newInstance());
        } catch (Exception e) {
            throw new IllegalArgumentException("Ignore invalid codec, newInstance failed: " + codecClass, e);
        }
    }

    /**
     * Install new codec
     *
     * @param codec new codec
     */
    public void installCodec(Codec codec) {
        this.installCodec(Collections.singleton(codec));
    }

    /**
     * install new codec into this CodecFactory
     *
     * @param codecs new codec
     */
    public void installCodec(Collection<Codec> codecs) {
        if (codecs == null) {
            return;
        }
        for (Codec codec : codecs) {
            if (codec == null || !this.codecs.add(codec)) {
                continue;
            }
            // collect all encode and decode methods
            for (Method method : codec.getClass().getDeclaredMethods()) {
                RealConverterMethod convertMethod = RealConverterMethod.valueOf(codec, method);
                if (convertMethod == null) {
                    continue;
                }
                converterMap.put(convertMethod);
                if (convertMethod.getSrcClass() != Object.class) {
                    xTypeFactory.addStopClass(convertMethod.getSrcClass());
                }
                if (convertMethod.getTgtClass() != Object.class) {
                    xTypeFactory.addStopClass(convertMethod.getTgtClass());
                }
            }
            codec.setFactory(this);
        }
        // reset all pipeline
        this.pipelineMap.clear();
    }

    /**
     * Convert the specified object to instance of the specified class
     *
     * @param src source data
     * @param clz target class
     * @param <T> Target template type
     * @return instance of the specified class
     */
    @SuppressWarnings("unchecked")
    public final <T> T convert(Object src, Class<T> clz) {
        return (T) this.doConvert(src, toXType(clz));
    }

    /**
     * Convert the specified object to the specified type
     *
     * @param src  Source data
     * @param type Target type
     * @return instance of the specified type
     */
    public final Object convert(Object src, Type type) {
        return this.doConvert(src, toXType(type));
    }

    /**
     * Execute data convert, convert src to the specified type target
     *
     * @param srcObj  Source Object
     * @param tgtType Target Type
     * @return Target Instance
     */
    protected Object doConvert(Object srcObj, XType tgtType) {
        Class srcClass = srcObj == null ? Object.class : srcObj.getClass();
        Class tgtClass = tgtType.getRawType();
        ConverterPipeline pipeline = this.getPipeline(srcClass, tgtClass);
        if (pipeline == null) {
            if (srcObj == null) {
                return null;
            }
            throw new IllegalStateException("Can't convert " + srcClass + " to " + tgtType);
        }
        return pipeline.convert(srcObj, tgtType);
    }

    public XType<?> toXType(Type type) {
        return xTypeFactory.toXType(type);
    }

    /**
     * Get the specified ConverterPipeline from srcClass to tgtClass
     *
     * @param srcClass Source class
     * @param tgtClass Target class
     * @return ConverterPipeline, could be null
     */
    public ConverterPipeline getPipeline(Class srcClass, Class tgtClass) {
        Long key = ((long) srcClass.hashCode()) << 32 | tgtClass.hashCode(); // TODO this is faster, but would go wrong???
        ConverterPipeline pipeline = pipelineMap.get(key);
        if (pipeline == null) {
            converterMap.flushCastConverter(srcClass);
            converterMap.flushCastConverter(tgtClass);
            // find the shortest path
            Path shortestPath = this.findShortestPath(srcClass, tgtClass);
            if (shortestPath != null) {
                pipeline = new ConverterPipeline(shortestPath.methods);
                pipelineMap.put(key, pipeline);
            }
        }
        return pipeline;
    }

    /**
     * Find the shortest path from srcClass to tgtClass
     *
     * @param srcClass Source Class
     * @param tgtClass Target Class
     * @return The shortest path, could be null
     */
    public Path findShortestPath(Class<?> srcClass, Class<?> tgtClass) {
        return this.findShortestPath(null, srcClass, tgtClass);
    }

    /**
     * Find the shortest path from srcClass to tgtClass
     * Don't need care about inherit, because all subclass->class will be represent by TranConvertMethod
     *
     * @param passed Passed router which shouldn't be used again
     */
    private Path findShortestPath(Map<Class, Integer> passed, Class<?> srcClass, Class<?> tgtClass) {
        if (srcClass == tgtClass) {
            ConverterMethod method = converterMap.get(srcClass, tgtClass);
            return new Path(0, method);
        }
        passed = passed == null ? new HashMap<>() : new HashMap<>(passed);
        passed.compute(srcClass, (clz, v) -> 1 + (v == null ? 0 : v));
        // calculate all path
        List<Path> paths = new ArrayList<>();
        for (ConverterMethod route : converterMap.get(srcClass)) {
            int passTimes = passed.getOrDefault(route.getTgtClass(), 0);
            if (route.getSrcClass() != route.getTgtClass() && passTimes >= 1) {
                continue; // for normal node, allow pass once
            }
            if (route.getSrcClass() == route.getTgtClass() && passTimes >= 2) {
                continue; // for self-converted node, allow repass once
            }
            Path path;
            if (route.isExtensible() && route.getTgtClass().isAssignableFrom(tgtClass)) {
                path = this.findShortestPath(passed, tgtClass, tgtClass);
            } else {
                path = this.findShortestPath(passed, route.getTgtClass(), tgtClass);
            }
            if (path != null) {
                paths.add(new Path(route, path));
            }
        }
        // find shortest path if exists
        if (!paths.isEmpty()) {
            paths.sort(Comparator.comparingInt(o -> o.distance));
        }
        return paths.isEmpty() ? null : paths.get(0);
    }

    public ConverterMap getConverterMap() {
        return converterMap;
    }

    /**
     * Converter's Path
     */
    static class Path {
        private final int                   distance;
        private final List<ConverterMethod> methods = new ArrayList<>();

        public Path(int distance, ConverterMethod method) {
            if (method != null) {
                this.methods.add(method);
            }
            this.distance = distance + (method == null ? 0 : method.getDistance());
        }

        public Path(ConverterMethod route, Path next) {
            this.distance = route.getDistance() + next.distance;
            this.methods.add(route);
            this.methods.addAll(next.methods);
        }
    }

}
