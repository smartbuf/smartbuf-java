package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.codec.array.primary.ByteArrayCodec;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author sulin
 * @since 2019-05-19 14:11:03
 */
public class CodecTest {

    @Test
    public void testSubMethod() {
        ByteArrayCodec codec = new ByteArrayCodec();

        for (Method method : codec.getClass().getMethods()) {
            System.out.println(method);
        }
        System.out.println("-------");
        for (Method method : codec.getClass().getDeclaredMethods()) {
            System.out.println(method);
            System.out.println(method.isBridge()); // 可以通过bridge判断是否是子类复写的转换方法
        }
    }

}