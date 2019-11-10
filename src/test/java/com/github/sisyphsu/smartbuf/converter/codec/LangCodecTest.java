package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.CodecFactory;
import com.github.sisyphsu.smartbuf.reflect.XTypeUtils;
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
        assert codec.toObject(emptyMap, XTypeUtils.toXType(Pojo.class)) != null;
        assert emptyMap == codec.toObject(emptyMap, XTypeUtils.toXType(Object.class));
    }

    @Test
    public void testPojo2() {
        Pojo pojo = new Pojo(1000L, "hello", Collections.singleton(1.999));
        Map map = codec.toMap(pojo);
        assert map != null;
        assert Objects.equals(map.get("id"), pojo.id);
        assert pojo.name == map.get("name");
        assert pojo.scores == map.get("scores");

        Object obj = codec.toObject(map, XTypeUtils.toXType(Pojo.class));
        assert obj instanceof Pojo;
        assert obj.equals(pojo);
        assert ((Pojo) obj).scores == pojo.scores;
    }

    @Test
    public void testInvalidPojo() {
        InvalidPojo pojo = new InvalidPojo("aa");
        Map map = codec.toMap(pojo);
        try {
            codec.toObject(map, XTypeUtils.toXType(InvalidPojo.class));
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
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

}
