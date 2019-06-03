package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Codec工程
 * 广度优先算法，扫描CodecPipeline
 *
 * @author sulin
 * @since 2019-05-20 16:14:54
 */
public class CodecFactory {

    private Set<Codec> codecs;

    private Map<Class, DecodeMethod> decodeSrcMap = new ConcurrentHashMap<>();
    private Map<Class, DecodeMethod> decodeTgtMap = new ConcurrentHashMap<>();
    private Map<Class, EncodeMethod> encodeSrcMap = new ConcurrentHashMap<>();
    private Map<Class, EncodeMethod> encodeTgtMap = new ConcurrentHashMap<>();

    private Map<PKey, DecodePipeline> decodePipelineMap = new ConcurrentHashMap<>();
    private Map<PKey, EncodePipeline> encodePipelineMap = new ConcurrentHashMap<>();

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
                    EncodeMethod em = new EncodeMethod(codec, method);
                    encodeSrcMap.put(em.getSrcClass(), em);
                    encodeTgtMap.put(em.getTgtClass(), em);
                }
                // collect DecodeMethod
                if (paramTypes.length == 2 && paramTypes[1] == Type.class && method.getReturnType() == codec.support()) {
                    DecodeMethod dm = new DecodeMethod(codec, method);
                    decodeSrcMap.put(dm.getSrcClass(), dm);
                    decodeTgtMap.put(dm.getTgtClass(), dm);
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
        PKey key = new PKey(src, tgt);
        DecodePipeline result = decodePipelineMap.get(key);
        if (result == null) {
            // TODO BFS find the shortest way
        }
        return result;
    }

    /**
     * Get EncodePipeline by the specified data convert direction.
     *
     * @param src source class
     * @param tgt target class
     * @return EncodePipeline, null means noway
     */
    public EncodePipeline getEncodePipeline(Class src, Class tgt) {
        PKey key = new PKey(src, tgt);
        EncodePipeline result = encodePipelineMap.get(key);
        if (result == null) {
            // TODO BFS find the shortest way
        }
        return null;
    }

    /**
     * Data convert pipeline's key
     */
    public static class PKey {

        private final Class src;
        private final Class tgt;

        public PKey(Class src, Class tgt) {
            this.src = src;
            this.tgt = tgt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            PKey that = (PKey) o;
            return src.equals(that.src) && tgt.equals(that.tgt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(src, tgt);
        }

    }

}
