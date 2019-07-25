package com.github.sisyphsu.nakedata.convertor.codec.exception;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.HashMap;
import java.util.Map;

/**
 * StackTraceElement's codec, support Initialize.
 *
 * @author sulin
 * @since 2019-05-13 20:47:16
 */
public class StackTraceElementCodec extends Codec {

    private static final String DECLARER = "declarer";
    private static final String METHOD = "method";
    private static final String FILE = "file";
    private static final String LINE = "line";

    /**
     * Convert Map to StackTraceElement
     *
     * @param map Map
     * @return StackTraceElement
     */
    @Converter
    public StackTraceElement toStackTraceElement(Map map) {
        if (map == null)
            return null;

        String declarer = convert(map.get(DECLARER), String.class);
        String method = convert(map.get(METHOD), String.class);
        String file = convert(map.get(FILE), String.class);
        Integer line = convert(map.get(LINE), Integer.class);
        if (line == null) {
            line = -1;
        }
        return new StackTraceElement(declarer, method, file, line);
    }

    /**
     * Convert StackTraceElement to Map
     *
     * @param element StackTraceElement
     * @return Map
     */
    @Converter
    public Map toMap(StackTraceElement element) {
        if (element == null)
            return null;
        Map<String, Object> result = new HashMap<>();
        result.put(DECLARER, element.getClassName());
        result.put(METHOD, element.getMethodName());
        result.put(FILE, element.getFileName());
        result.put(LINE, element.getLineNumber());
        return result;
    }

}
