package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.BeanInfo;
import com.github.sisyphsu.smartbuf.converter.CodecFactory;
import com.github.sisyphsu.smartbuf.reflect.BeanField;
import com.github.sisyphsu.smartbuf.reflect.BeanWriter;
import com.github.sisyphsu.smartbuf.reflect.BeanWriterBuilder;
import com.github.sisyphsu.smartbuf.reflect.XTypeUtils;
import com.github.sisyphsu.smartbuf.utils.CodecUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author sulin
 * @since 2019-08-04 17:46:15
 */
public class LangCodecTest {

    private LangCodec codec = new LangCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() throws Exception {
        String pattern = "yyyy-MM-dd hh:mm:ss.SSS";
        assert Objects.equals(pattern, codec.toString(codec.toSimpleDateFormat(pattern)));

        String clzName = "java.util.List";
        assert Objects.equals(codec.toString(codec.toClass(clzName)), clzName);

        assert codec.toEnum(codec.toString(TimeUnit.SECONDS), XTypeUtils.toXType(TimeUnit.class)) == TimeUnit.SECONDS;
    }

    @Test
    public void testPojo1() {
        Map emptyMap = new HashMap();
        codec.toObject(emptyMap, XTypeUtils.toXType(Pojo.class));

        BeanWriter writer = BeanWriterBuilder.build(Pojo.class);
        try {
            codec.toObject(new BeanInfo(writer, new Object[0]), XTypeUtils.toXType(Pojo.class));
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        writer = new BeanWriter(null, new BeanField[0]);
        try {
            codec.toObject(new BeanInfo(writer, new Object[3]), XTypeUtils.toXType(Pojo.class));
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    @Test
    public void testPojo2() {
        Pojo pojo = new Pojo(1000L, "hello", Collections.singleton(1.999));
        Map map = codec.toMap(pojo);
        assert Objects.equals(map.get("id"), pojo.id);
        assert pojo.name == map.get("name");
        assert pojo.scores == map.get("scores");

        Object obj = codec.toObject(codec.toObject(map, XTypeUtils.toXType(Pojo.class)), XTypeUtils.toXType(Pojo.class));
        assert obj instanceof Pojo;
        assert obj.equals(pojo);
        assert ((Pojo) obj).scores == pojo.scores;
    }

    @Test
    public void testInvalidPojo() {
        InvalidPojo pojo = new InvalidPojo("aa");
        Map map = codec.toMap(pojo);

        try {
            CodecUtils.convert(map, InvalidPojo.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    @Test
    public void testLang() {
        Map<String, Object> map = new HashMap<>();
        map.put("z", "true");
        map.put("b", "1");
        map.put("s", "10");
        map.put("i", "100");
        map.put("j", "1000");
        map.put("f", "1.0");
        map.put("d", "10.0");
        Lang l = CodecUtils.convert(map, Lang.class);

        assert l.z;
        assert l.b == 1;
        assert l.s == 10;
        assert l.i == 100;
        assert l.j == 1000;
        assert l.f == 1.0f;
        assert l.d == 10.0;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pojo {
        private long        id;
        private String      name;
        private Set<Double> scores;
    }

    @Data
    @AllArgsConstructor
    public static class InvalidPojo {
        private String key;
    }

    @Data
    public static class Lang {
        private boolean z;
        private byte    b;
        private short   s;
        private int     i;
        private long    j;
        private float   f;
        private double  d;
    }

}
