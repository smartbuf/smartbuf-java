package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CodecFactory, meantains CodecMethod and Pipeline.
 *
 * @author sulin
 * @since 2019-05-20 16:14:54
 */
public class CodecFactory {

    public static final CodecFactory Instance = new CodecFactory(null);

    private Set<Codec> codecs = new HashSet<>();
    private MMap<ConverterMethod> methodMap = new MMap<>();
    private MMap<ConverterPipeline> pipelineMap = new MMap<>();

    /**
     * Initialize CodecFactory with the specified Codec type.
     *
     * @param codecs Codec type
     */
    public CodecFactory(Set<Codec> codecs) {
        for (Class<? extends Codec> codecCls : CodecScanner.scanAllCodecs()) {
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
                ConverterMethod convertMethod = ConverterMethod.valueOf(codec, method);
                if (convertMethod == null) {
                    continue;
                }
                methodMap.put(convertMethod.getSrcClass(), convertMethod.getTgtClass(), convertMethod);
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
        Class tgtClass = tgtType.getClass();
        ConverterPipeline pipeline = pipelineMap.get(srcClass, tgtClass);
        if (pipeline == null) {
            Path shortestPath = this.findShortestPath(null, srcClass, tgtClass);
            if (shortestPath != null) {
                pipeline = new ConverterPipeline(srcClass, tgtClass, shortestPath.methods);
                pipelineMap.put(srcClass, tgtClass, pipeline);
            }
        }
        if (pipeline == null) {
            throw new IllegalStateException("Can't convert " + srcObj.getClass() + " to " + tgtType);
        }
        return pipeline.convert(srcObj, tgtType);
    }

    /**
     * Find the shortest from srcClass to tgtClass
     *
     * @param passed   Passed router which shouldn't be used again
     * @param srcClass Source Class
     * @param tgtClass Target Class
     * @return The shortest path, could be null
     */
    private Path findShortestPath(Set<Class> passed, Class srcClass, Class tgtClass) {
        if (srcClass == tgtClass) {
            ConverterMethod method = methodMap.get(srcClass, srcClass);
            return new Path(0, method);
        }
        passed = passed == null ? new HashSet<>() : new HashSet<>(passed);
        passed.add(srcClass);
        // calculate all path
        List<Path> paths = new ArrayList<>();
        Collection<ConverterMethod> routes = methodMap.get(srcClass);
        for (ConverterMethod route : routes) {
            if (passed.contains(route.getTgtClass())) {
                continue; // ignore passed node
            }
            Path path = this.findShortestPath(passed, route.getTgtClass(), tgtClass);
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

    /**
     * MultiMap, like MultiKeyMap.
     */
    private static class MMap<T> {

        private Map<Class, Map<Class, T>> map = new ConcurrentHashMap<>();

        public void put(Class srcType, Class tgtType, T method) {
            map.computeIfAbsent(srcType, (c) -> new ConcurrentHashMap<>()).put(tgtType, method);
        }

        public Collection<T> get(Class srcType) {
            Map<Class, T> tgtMap = map.get(srcType);
            if (tgtMap == null) {
                return null;
            }
            return tgtMap.values();
        }

        public T get(Class srcType, Class tgtType) {
            Map<Class, T> tgtMap = map.get(srcType);
            if (tgtMap == null) {
                return null;
            }
            return tgtMap.get(tgtType);
        }

        public void clear() {
            this.map.clear();
        }

    }

    /**
     * Converter's Path
     */
    private static class Path {
        private final int distance;
        private final List<ConverterMethod> methods = new ArrayList<>();

        public Path(int distance, ConverterMethod method) {
            this.distance = distance;
            if (method != null) {
                this.methods.add(method);
            }
        }

        public Path(ConverterMethod route, Path next) {
            this.distance = route.getDistance() + next.distance;
            this.methods.add(route);
            this.methods.addAll(next.methods);
        }
    }

}
