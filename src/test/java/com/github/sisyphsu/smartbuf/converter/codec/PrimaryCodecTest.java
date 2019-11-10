package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-08-05 19:49:55
 */
class PrimaryCodecTest {

    private PrimaryCodec codec = new PrimaryCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        assert codec.toBoolean(codec.toBoolean(true));
        assert codec.toByte(codec.toByte((byte) 1)) == 1;
        assert codec.toShort(codec.toShort((short) 1)) == 1;
        assert codec.toInt(codec.toInteger(1)) == 1;
        assert codec.toLong(codec.toLong(1L)) == 1L;
        assert codec.toFloat(codec.toFloat(1.0f)) == 1.0f;
        assert codec.toDouble(codec.toDouble(1.0)) == 1.0;
        assert codec.toChar(codec.toCharacter('a')) == 'a';
    }

}
