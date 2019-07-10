package com.github.sisyphsu.nakedata.convertor.codec.exception;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.Map;

/**
 * StackTraceElement's codec, support Initialize.
 *
 * @author sulin
 * @since 2019-05-13 20:47:16
 */
public class StackTraceElementCodec extends Codec {

    /**
     * Convert Map to StackTraceElement
     *
     * @param map Map
     * @return StackTraceElement
     */
    public StackTraceElement toStackTraceElement(Map map) {
        if (map == null)
            return null;
        String declaringClass = convert(map.get("declaringClass"), String.class);
        String methodName = convert(map.get("methodName"), String.class);
        String fileName = convert(map.get("fileName"), String.class);
        Integer lineNumber = convert(map.get("lineNumber"), Integer.class);
        if (lineNumber == null) {
            lineNumber = -1;
        }
        return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
    }

}
