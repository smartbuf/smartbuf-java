package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

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

        assert codec.toBoolean(codec.toLong(true));
        assert codec.toBoolean(codec.toString(true));

        Character c = 'C';
        assert c.equals(codec.toCharacter(codec.toInteger(c)));
    }

    @Test
    public void testString() {
        Double d = 10000.00000123;
        System.out.println(d);
        System.out.printf("%f\n", d);

        Float f = 0.0000001f;
        System.out.println(f);
        System.out.printf("%f\n", f);

        DecimalFormat format = new DecimalFormat("0.##########");
        System.out.println(format.format(d));
        System.out.println(format.format(f));
    }

}