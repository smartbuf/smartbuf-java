package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.reflect.XType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Codec for Throwable, support standard Throwable and StackTraceElement
 *
 * @author sulin
 * @since 2019-05-13 20:43:35
 */
@Slf4j
public final class ThrowableCodec extends Codec {

    private static final String F_DECLARER = "declarer";
    private static final String F_METHOD   = "method";
    private static final String F_FILE     = "file";
    private static final String F_LINE     = "line";

    public static final String E_TYPE   = "type";
    public static final String E_MSG    = "msg";
    public static final String E_CAUSE  = "cause";
    public static final String E_STACKS = "stacks";

    /**
     * Convert Map to StackTraceElement
     */
    @Converter
    public StackTraceElement toStackTraceElement(Map map) {
        String declarer = convert(map.get(F_DECLARER), String.class);
        String method = convert(map.get(F_METHOD), String.class);
        String file = convert(map.get(F_FILE), String.class);
        Integer line = convert(map.get(F_LINE), Integer.class);
        if (line == null) {
            line = -1;
        }
        return new StackTraceElement(declarer, method, file, line);
    }

    /**
     * Convert StackTraceElement to Map
     */
    @Converter
    public Map toMap(StackTraceElement element) {
        Map<String, Object> result = new HashMap<>();
        result.put(F_DECLARER, element.getClassName());
        result.put(F_METHOD, element.getMethodName());
        result.put(F_FILE, element.getFileName());
        result.put(F_LINE, element.getLineNumber());
        return result;
    }

    /**
     * Convert Throwable to Map
     */
    @Converter
    public Map toMap(Throwable t) {
        Map<String, Object> result = new HashMap<>();
        result.put(E_TYPE, t.getClass().getName());
        result.put(E_MSG, t.getMessage());
        result.put(E_CAUSE, t.getCause());
        result.put(E_STACKS, t.getStackTrace());
        return result;
    }

    /**
     * Convert Map to Throwable
     */
    @Converter
    public Throwable toThrowable(Map map, XType type) {
        String eType = convert(map.get(E_TYPE), String.class);
        // Decide class
        Class<?> clz = type.getRawType();
        if (clz == Throwable.class && eType != null) {
            try {
                Class<?> eClz = Class.forName(eType);
                if (Throwable.class.isAssignableFrom(eClz)) {
                    clz = eClz;
                } else {
                    log.debug("Find invalid etype[{}], as Throwable as default", eType);
                }
            } catch (ClassNotFoundException ignored) {
                log.debug("Can't find class: {}, use Throwable as default", eType);
            }
        }
        // Create Throwable
        String eMsg = convert(map.get(E_MSG), String.class);
        Throwable cause = convert(map.get(E_CAUSE), Throwable.class);
        Throwable result = createThrowable(clz, eMsg, cause);
        if (map.containsKey(E_STACKS)) {
            result.setStackTrace(convert(map.get(E_STACKS), StackTraceElement[].class));
        }
        return result;
    }

    /**
     * Create Throwable by the specified Class and parameters
     *
     * @param clz   Throwable class
     * @param msg   message parameter
     * @param cause cause parameter
     * @return Throwable
     */
    public static Throwable createThrowable(Class<?> clz, String msg, Throwable cause) {
        if (!Throwable.class.isAssignableFrom(clz)) {
            throw new IllegalArgumentException("invalid Throwable type: " + clz);
        }
        // try Throwable(msg, cause)
        Throwable result = null;
        try {
            result = (Throwable) clz.getConstructor(String.class, Throwable.class).newInstance(msg, cause);
        } catch (Exception ignored) {
        }
        // try Throwable(msg)
        if (result == null) {
            try {
                result = (Throwable) clz.getConstructor(String.class).newInstance(msg);
            } catch (Exception ignored) {
            }
        }
        // try Throwable(cause)
        if (result == null) {
            try {
                result = (Throwable) clz.getConstructor(Throwable.class).newInstance(cause);
            } catch (Exception ignored) {
            }
        }
        // try Throwable()
        if (result == null) {
            try {
                result = (Throwable) clz.getConstructor().newInstance();
            } catch (Exception ignored) {
            }
        }
        if (result == null) {
            throw new UnsupportedOperationException("Can't initialize Throwable: " + clz);
        }
        return result;
    }

}
