package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * Number's codec
 *
 * @author sulin
 * @since 2019-05-13 18:14:18
 */
public class NumberCodec extends Codec {

    /**
     * Convert Long to Number
     *
     * @param l Long
     * @return Number
     */
    public Number toNumber(Long l) {
        return l;
    }

}
