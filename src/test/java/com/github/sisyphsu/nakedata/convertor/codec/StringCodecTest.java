package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-08-04 18:58:48
 */
public class StringCodecTest {

    private StringCodec codec = new StringCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        String str = "hello world";
        assert str.equals(codec.toString(codec.toStringBuffer(str)));
        assert str.equals(codec.toString(codec.toStringBuilder(str)));
        assert str.equals(codec.toString(codec.toCharArray(str)));
        assert str.equals(codec.toString(codec.toByteArray(str)));
        assert str.equals(codec.toString(codec.toStringCharacterIterator(str)));

        String s = "s";
        assert s.equals(codec.toString(codec.toCharacter(s)));
    }

}