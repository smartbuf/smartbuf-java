package com.github.sisyphsu.nakedata.convertor.codec.sql;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.sql.Clob;

/**
 * Clob's codec
 *
 * @author sulin
 * @since 2019-05-13 18:10:38
 */
public class ClobCodec extends Codec {

    public String toString(Clob c) {
        if (c == null)
            return null;
        return c.toString(); // TODO
    }

}
