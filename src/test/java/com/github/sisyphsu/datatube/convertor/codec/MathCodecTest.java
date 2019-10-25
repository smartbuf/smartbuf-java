package com.github.sisyphsu.datatube.convertor.codec;

import com.github.sisyphsu.datatube.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author sulin
 * @since 2019-08-04 18:29:52
 */
public class MathCodecTest {

    private MathCodec codec = new MathCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        BigDecimal decimal = new BigDecimal("199999.222232323134123412312312312312312312312");
        BigInteger integer = new BigInteger("999999999999999999999999999999999999999999");

        assert decimal.equals(codec.toBigDecimal(codec.toString(decimal)));
        assert integer.equals(codec.toBigInteger(codec.toString(integer)));
    }

}
