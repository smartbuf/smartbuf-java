package com.github.sisyphsu.canoe.convertor;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
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

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(null);
            CodecScanner.scanCodecs("com.github.sisyphsu");
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }

        try {
            Thread.currentThread().setContextClassLoader(new ClassLoader() {
                @Override
                public Enumeration<URL> getResources(String name) throws IOException {
                    throw new IOException();
                }
            });
            CodecScanner.scanCodecs("tmp.com.github.sisyphsu");
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        Thread.currentThread().setContextClassLoader(loader);
    }

    @Test
    public void scanAllClasses2() throws IOException {
        String tmpdir = System.getProperty("java.io.tmpdir");

        File file = new File(tmpdir + File.separator + "XXXXXXXXXXXXXXXXXXXE.class");
        IOUtils.write("test", new FileOutputStream(file), Charset.defaultCharset());

        List<Class> classes = CodecScanner.scanAllClasses(file, "com.teeeeee");
        assert classes.size() == 0;

        classes = CodecScanner.scanAllClasses(new File(tmpdir + File.separator + System.currentTimeMillis()), "com.teeeeee");
        assert classes.size() == 0;

        CodecScanner.scanAllClasses(new File(tmpdir), "");
    }

}
