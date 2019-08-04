package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.reflect.XType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
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
public class CodecFactory {

    public static final CodecFactory Instance = new CodecFactory(null);

    private final Set<Codec> codecs = ConcurrentHashMap.newKeySet();
    private final ConverterMap converterMap = new ConverterMap();
    private final Map<PKey, ConverterPipeline> pipelineMap = new ConcurrentHashMap<>();

    /**
     * Initialize CodecFactory with the specified Codec type.
     *
     * @param codecs Codec type
     */
    public CodecFactory(Set<Codec> codecs) {
        for (Class<? extends Codec> codecCls : CodecScanner.scanCodecs()) {
            this.installCodec(codecCls);
        }
        if (codecs != null) {
            this.installCodec(codecs); // init index
        }
    }

    /**
     * Install new codec
     *
     * @param codecClass new codec's class
     */
    public void installCodec(Class<? extends Codec> codecClass) {
        Codec codec;
        try {
            codec = codecClass.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid codec's class, newInstance failed.", e);
        }
        this.installCodec(codec);
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
    public void installCodec(Set<Codec> codecs) {
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
            }
            codec.setFactory(this);
        }
        // reset all pipeline
        this.pipelineMap.clear();
    }

    /**
     * Execute data convert, convert src to the specified type target
     *
     * @param srcObj  Source Object
     * @param tgtType Target Type
     * @return Target Instance
     */
    public Object doConvert(Object srcObj, XType tgtType) {
        if (srcObj == null) {
            return null;
        }
        Class srcClass = srcObj.getClass();
        Class tgtClass = tgtType.getRawType();
        ConverterPipeline pipeline = this.getPipeline(srcClass, tgtClass);
        if (pipeline == null) {
            throw new IllegalStateException("Can't convert " + srcObj.getClass() + " to " + tgtType);
        }
        return pipeline.convert(srcObj, tgtType);
    }

    /**
     * Get the specified ConverterPipeline from srcClass to tgtClass
     *
     * @param srcClass Source class
     * @param tgtClass Target class
     * @return ConverterPipeline, could be null
     */
    protected ConverterPipeline getPipeline(Class srcClass, Class tgtClass) {
        ConverterPipeline pipeline = pipelineMap.get(new PKey(srcClass, tgtClass));
        if (pipeline == null) {
            // find the shortest path
            Path shortestPath = this.findShortestPath(null, srcClass, tgtClass);
            if (shortestPath != null) {
                pipeline = new ConverterPipeline(srcClass, tgtClass, shortestPath.methods);
                pipelineMap.put(new PKey(srcClass, tgtClass), pipeline);
            }
        }
        return pipeline;
    }

    /**
     * Find the shortest path from srcClass to tgtClass
     * Don't need care about inherit, because all subclass->class will be represent by TranConvertMethod
     *
     * @param passed   Passed router which shouldn't be used again
     * @param srcClass Source Class
     * @param tgtClass Target Class
     * @return The shortest path, could be null
     */
    protected Path findShortestPath(Set<Class<?>> passed, Class<?> srcClass, Class<?> tgtClass) {
        if (srcClass == tgtClass) {
            ConverterMethod method = converterMap.get(srcClass, tgtClass);
            return new Path(0, method);
        }
        passed = passed == null ? new HashSet<>() : new HashSet<>(passed);
        passed.add(srcClass);
        // calculate all path
        List<Path> paths = new ArrayList<>();
        Collection<ConverterMethod> routes = converterMap.get(srcClass);
        for (ConverterMethod route : routes) {
            if (passed.contains(route.getTgtClass())) {
                continue;  // ignore passed node, TODO but sometimes need repass same node
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
    private static class Path {
        private final int distance;
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

    /**
     * Pipeline's key, used for Map
     */
    @Data
    @AllArgsConstructor
    private static class PKey {
        private final Class<?> srcClass;
        private final Class<?> tgtClass;
    }

}
