package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
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
    private MMap<ConvertMethod> methodMap = new MMap<>();
    private MMap<ConvertPipeline> pipelineMap = new MMap<>();

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
                ConvertMethod convertMethod = ConvertMethod.valueOf(codec, method);
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
        ConvertPipeline pipeline = pipelineMap.get(srcClass, tgtClass);
        if (pipeline == null) {
            List<ConvertMethod> methods = this.dfs(srcClass, tgtClass, methodMap);
            if (methods != null && methods.size() > 0) {
                pipeline = ConvertPipeline.valueOf(methods);
                pipelineMap.put(srcClass, tgtClass, pipeline);
            }
        }
        if (pipeline == null) {
            throw new IllegalStateException("Can't convert " + srcObj.getClass() + " to " + tgtType);
        }
        return pipeline.convert(srcObj, tgtType);
    }

    /**
     * Search the shortest codec path
     */
    private List<ConvertMethod> dfs(Class srcClass, Class tgtClass, MMap<ConvertMethod> map) {
        ConvertMethod direct = map.get(srcClass, tgtClass);
        if (direct != null) {
            return Collections.singletonList(direct); // directly
        }
        Collection<ConvertMethod> ts = map.get(srcClass);
        if (ts == null || ts.isEmpty()) {
            return null; // noway
        }
        // find shortest way
        ConvertMethod router = null;
        List<ConvertMethod> subResult = null;
        for (ConvertMethod t : ts) {
            List<ConvertMethod> tmp = this.dfs(t.getTgtClass(), tgtClass, map);
            if (tmp == null || tmp.isEmpty()) {
                continue;
            }
            if (subResult == null || subResult.size() > tmp.size()) {
                subResult = tmp;
                router = t;
            }
        }
        if (subResult == null) {
            return null; // noway
        }
        List<ConvertMethod> result = new ArrayList<>();
        result.add(router);
        result.addAll(subResult);
        return result;
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

}
