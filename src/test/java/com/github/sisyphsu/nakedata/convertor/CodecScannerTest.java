package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import org.junit.Test;

import java.util.Set;

/**
 * Test CodecScanner
 *
 * @author sulin
 * @since 2019-06-03 20:52:17
 */
public class CodecScannerTest {

    @Test
    public void loadAllCodec() {
        Set<Class<? extends Codec>> codecs = CodecScanner.scanAllCodecs();
        for (Class<? extends Codec> codec : codecs) {
            System.out.println(codec);
        }
    }

}