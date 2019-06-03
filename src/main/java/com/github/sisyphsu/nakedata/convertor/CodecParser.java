package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parse Codec into EncodeMethod and DecodeMethod.
 *
 * @author sulin
 * @since 2019-06-03 11:26:58
 */
public class CodecParser {

    public static List<EncodeMethod> parseEncodeMethods(Codec codec) {
        List<EncodeMethod> result = new ArrayList<>();
        if (codec == null) {
            return result;
        }
        for (Method method : codec.getClass().getDeclaredMethods()) {
            if (method.isBridge() || method.isVarArgs() || method.isDefault() || method.isSynthetic()) {
                continue; // ignore
            }
            Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1 || !Objects.equals(paramTypes[0], codec.support())) {
                continue; // not encode
            }
            result.add(new EncodeMethod(codec, method));
        }
        return result;
    }

    public static List<DecodeMethod> parseDecodeMethods(Codec codec) {
        List<DecodeMethod> result = new ArrayList<>();
        if (codec == null) {
            return result;
        }
        for (Method method : codec.getClass().getDeclaredMethods()) {
            if (method.isBridge() || method.isVarArgs() || method.isDefault() || method.isSynthetic()) {
                continue; // ignore
            }
            Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1 || !Objects.equals(paramTypes[0], codec.support())) {
                continue; // not encode
            }
            result.add(new DecodeMethod(codec, method));
        }
        return result;
    }

}
