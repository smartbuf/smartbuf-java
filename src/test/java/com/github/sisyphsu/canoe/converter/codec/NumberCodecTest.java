package com.github.sisyphsu.canoe.converter.codec;

import com.github.sisyphsu.canoe.converter.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-08-04 18:58:23
 */
public class NumberCodecTest {

    private NumberCodec codec = new NumberCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        String str = "1234567890";
        assert str.equals(codec.toString(codec.toLong(str)));

        assert Long.valueOf(100L).equals(codec.toLong(codec.toByte(100L)));
        assert Long.valueOf(10000L).equals(codec.toLong(codec.toShort(10000L)));
        assert Long.valueOf(100000000L).equals(codec.toLong(codec.toInteger(100000000L)));

        Double d = 1234567890.123456;
        Float f = 123.4567F;
        assert Math.abs(codec.toDouble(codec.toString(d)) - d) < 0.0001;
        assert codec.toDouble(codec.toLong(d)) == 1234567890;
        assert Math.abs(codec.toFloat(codec.toDouble(f)) - f) < 0.01; // precision lost
        assert Math.abs(codec.toFloat(codec.toString(f)) - f) < 0.01;

        assert codec.toBoolean(codec.toLong(true));
        assert !codec.toBoolean(codec.toLong(false));
        assert codec.toBoolean(codec.toString(true));

        Character c = 'C';
        assert c.equals(codec.toCharacter(codec.toInteger(c)));
    }

}
