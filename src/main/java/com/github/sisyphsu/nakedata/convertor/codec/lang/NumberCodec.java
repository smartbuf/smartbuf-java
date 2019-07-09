package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Number's codec
 *
 * @author sulin
 * @since 2019-05-13 18:14:18
 */
public class NumberCodec extends Codec {

    public Number toNumber(Byte b) {
        return b;
    }

    public Number toNumber(Short b) {
        return b;
    }

    public Number toNumber(Integer b) {
        return b;
    }

    public Number toNumber(Long b) {
        return b;
    }

    public Number toNumber(Float f) {
        return f;
    }

    public Number toNumber(Double d) {
        return d;
    }

    public Number toNumber(BigInteger i) {
        return i;
    }

    public Number toNumber(BigDecimal d) {
        return d;
    }

}
