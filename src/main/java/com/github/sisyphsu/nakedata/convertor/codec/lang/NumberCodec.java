package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Number's codec
 *
 * @author sulin
 * @since 2019-05-13 18:14:18
 */
public class NumberCodec extends Codec {

    @Converter
    public Number toNumber(Byte b) {
        return b;
    }

    @Converter
    public Number toNumber(Short b) {
        return b;
    }

    @Converter
    public Number toNumber(Integer b) {
        return b;
    }

    @Converter
    public Number toNumber(Long b) {
        return b;
    }

    @Converter
    public Number toNumber(Float f) {
        return f;
    }

    @Converter
    public Number toNumber(Double d) {
        return d;
    }

    @Converter
    public Number toNumber(BigInteger i) {
        return i;
    }

    @Converter
    public Number toNumber(BigDecimal d) {
        return d;
    }

}
