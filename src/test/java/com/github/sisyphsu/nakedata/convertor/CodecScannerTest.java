package com.github.sisyphsu.nakedata.convertor;

import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Test CodecScanner
 *
 * @author sulin
 * @since 2019-06-03 20:52:17
 */
public class CodecScannerTest {

    @Test
    public void scanAllCodecs() {
        Set<Class<? extends Codec>> codecs = CodecScanner.scanCodecs();
        for (Class<? extends Codec> codec : codecs) {
            System.out.println(codec);
        }
    }

    @Test
    public void scanAllClasses() {
        Set<Class<? extends Codec>> classes = CodecScanner.scanCodecs("com.github.sisyphsu");
        for (Class<? extends Codec> codecCls : classes) {
            System.out.println(codecCls);
        }
    }

}