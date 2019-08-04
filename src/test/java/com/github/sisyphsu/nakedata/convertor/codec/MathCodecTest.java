package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author sulin
 * @since 2019-08-04 18:29:52
 */
public class MathCodecTest {

    private static MathCodec codec = new MathCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

    @Test
    public void test() {
        BigDecimal decimal = new BigDecimal("199999.222232323134123412312312312312312312312");
        BigInteger integer = new BigInteger("999999999999999999999999999999999999999999");

        assert decimal.equals(codec.toBigDecimal(codec.toString(decimal)));
        assert integer.equals(codec.toBigInteger(codec.toString(integer)));
    }

}