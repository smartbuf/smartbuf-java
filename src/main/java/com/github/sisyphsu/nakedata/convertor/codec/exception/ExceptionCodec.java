package com.github.sisyphsu.nakedata.convertor.codec.exception;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * @author sulin
 * @since 2019-05-13 20:43:35
 */
public class ExceptionCodec extends Codec {

    public Exception toException(Throwable t) {
        if (t == null)
            return null;
        Exception e = new Exception(t.getMessage(), t.getCause());
        e.setStackTrace(t.getStackTrace());
        return e;
    }

    public <T extends Throwable> T toException(Throwable e, Type type) {
        // get
        Class eType = (Class) type;
        T result = null;
        try {
            Constructor<?>[] constructors = eType.getDeclaredConstructors();
            // ()
            // (String)
            // (String, Throwable)
            result = (T) eType.newInstance();
            result.setStackTrace(e.getStackTrace());
        } catch (Exception ex) {
            throw new RuntimeException("");
        }
        return result;
    }

}
