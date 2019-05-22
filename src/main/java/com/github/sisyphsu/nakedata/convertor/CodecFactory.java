package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Codec工程
 *
 * @author sulin
 * @since 2019-05-20 16:14:54
 */
public class CodecFactory {

    private static List<Codec> codecs;


    public static class Index {
        private final Map<Class, CodecMethod> sourceMap = new HashMap<>();
        private final Map<Class, CodecMethod> targetMap = new HashMap<>();

    }

}
