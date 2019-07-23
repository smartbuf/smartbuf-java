package com.github.sisyphsu.nakedata.convertor.codec.sql;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.sql.Clob;
import java.sql.SQLException;

/**
 * Clob's codec
 *
 * @author sulin
 * @since 2019-05-13 18:10:38
 */
public class ClobCodec extends Codec {

    /**
     * Convert Clob to String
     *
     * @param c Clob
     * @return String
     */
    @Converter
    public String toString(Clob c) {
        if (c == null)
            return null;
        try {
            return convert(c.getCharacterStream(), String.class);
        } catch (SQLException e) {
            throw new RuntimeException("Convert Clob to String failed", e);
        }
    }

}
