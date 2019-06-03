package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.pipeline.*;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * CodecFactory, meantains CodecMethod and Pipeline.
 *
 * @author sulin
 * @since 2019-05-20 16:14:54
 */
public class CodecFactory {

    private Set<Codec> codecs;

    private CodecMap<DecodeMethod> decodeMap = new CodecMap<>();
    private CodecMap<EncodeMethod> encodeMap = new CodecMap<>();

    private Map<PipelineKey, DecodePipeline> decodePipelineMap = new ConcurrentHashMap<>();
    private Map<PipelineKey, EncodePipeline> encodePipelineMap = new ConcurrentHashMap<>();

    /**
     * Initialize CodecFactory with the specified Codec type.
     *
     * @param codecs Codec type
     */
    public CodecFactory(Set<Codec> codecs) {
        this.codecs = new ConcurrentSkipListSet<>(codecs);
        // init index
        this.installCodec(codecs);
    }

    /**
     * Install new codec
     *
     * @param codecClass new codec's class
     */
    public void installCodec(Class<Codec> codecClass) {
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
                if (method.isBridge() || method.isVarArgs() || method.isDefault() || method.isSynthetic()) {
                    continue; // ignore flags
                }
                if (method.getReturnType() == Void.class) {
                    continue; // ignore void return
                }
                Class[] paramTypes = method.getParameterTypes();
                // collect EncodeMethod
                if (paramTypes.length == 1 && paramTypes[0] == codec.support()) {
                    encodeMap.put(new EncodeMethod(codec, method));
                }
                // collect DecodeMethod
                if (paramTypes.length == 2 && paramTypes[1] == Type.class && method.getReturnType() == codec.support()) {
                    decodeMap.put(new DecodeMethod(codec, method));
                }
            }
        }
        // reset all pipeline
        this.decodePipelineMap.clear();
        this.encodePipelineMap.clear();
    }

    /**
     * Get DecodePipeline by the specified data convert direction.
     *
     * @param src source class
     * @param tgt target class
     * @return DecodePipeline, null means noway.
     */
    public DecodePipeline getDecodePipeline(Class src, Class tgt) {
        PipelineKey key = new PipelineKey(src, tgt);
        return decodePipelineMap.computeIfAbsent(key, (k) -> {
            List<DecodeMethod> methods = this.dfs(src, tgt, decodeMap);
            if (methods == null || methods.size() == 0) {
                return null;
            }
            return new DecodePipeline(methods.toArray(new DecodeMethod[0]));
        });
    }

    /**
     * Get EncodePipeline by the specified data convert direction.
     *
     * @param src source class
     * @param tgt target class
     * @return EncodePipeline, null means noway
     */
    public EncodePipeline getEncodePipeline(Class src, Class tgt) {
        PipelineKey key = new PipelineKey(src, tgt);
        return encodePipelineMap.computeIfAbsent(key, (k) -> {
            List<EncodeMethod> methods = this.dfs(src, tgt, encodeMap);
            if (methods == null || methods.isEmpty()) {
                return null;
            }
            return new EncodePipeline(methods.toArray(new EncodeMethod[0]));
        });
    }

    /**
     * Search the shortest codec path
     */
    private <T extends CodecMethod> List<T> dfs(Class src, Class tgt, CodecMap<T> map) {
        T direct = map.get(src, tgt);
        if (direct != null) {
            return Collections.singletonList(direct); // directly
        }
        Collection<T> ts = map.get(src);
        if (ts == null || ts.isEmpty()) {
            return null; // noway
        }
        // find shortest way
        T router = null;
        List<T> subResult = null;
        for (T t : ts) {
            List<T> tmp = this.dfs(t.getTgtClass(), tgt, map);
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
        List<T> result = new ArrayList<>();
        result.add(router);
        result.addAll(subResult);
        return result;
    }

    /**
     * CodecMap, like MultiKeyMap, meantains codec tree.
     *
     * @param <T> DecodeMethod or EncodeMethod
     */
    public static class CodecMap<T extends CodecMethod> {

        private Map<Class, Map<Class, T>> map = new ConcurrentHashMap<>();

        public void put(T t) {
            Map<Class, T> tgtMap = map.computeIfAbsent(t.getSrcClass(), (c) -> new ConcurrentHashMap<>());
            tgtMap.put(t.getTgtClass(), t);
        }

        public Collection<T> get(Class srcClass) {
            Map<Class, T> tgtMap = map.get(srcClass);
            if (tgtMap == null) {
                return null;
            }
            return tgtMap.values();
        }

        public T get(Class srcClass, Class tgtClass) {
            Map<Class, T> tgtMap = map.get(srcClass);
            if (tgtMap == null) {
                return null;
            }
            return tgtMap.get(tgtClass);
        }

    }

}
