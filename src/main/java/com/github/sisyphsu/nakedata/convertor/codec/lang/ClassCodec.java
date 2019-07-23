package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * Class's codec
 *
 * @author sulin
 * @since 2019-05-13 18:58:12
 */
public class ClassCodec extends Codec {

    /**
     * Convert String to Class
     *
     * @param name String
     * @return Class
     */
    @Converter
    public Class toClass(String name) {
        if (name == null || name.isEmpty())
            return null;
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Convert String to Class failed: " + name);
        }
    }

    /**
     * Convert Class to String
     *
     * @param cls Class
     * @return String
     */
    @Converter
    public String toString(Class cls) {
        return cls == null ? null : cls.getName();
    }

}
