package com.github.sisyphsu.nakedata.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.ReflectUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 测试Object转换为Map的性能
 * <p>
 * 测试Bean实例, Jackson在功能上会额外递归转换
 * Benchmark                                   Mode  Cnt  Score   Error  Units
 * ConvertMapBenchmark.beanutilsBenchmark      avgt    6  1.172 ± 0.017  us/op
 * ConvertMapBenchmark.cglibBenchmark          avgt    6  0.060 ± 0.002  us/op
 * ConvertMapBenchmark.jacksonBenchmark        avgt    6  0.755 ± 0.035  us/op
 * ConvertMapBenchmark.jacksonToTreeBenchmark  avgt    6  0.715 ± 0.014  us/op
 * <p>
 * 测试Egg实例
 * Benchmark                                   Mode  Cnt  Score   Error  Units
 * ConvertMapBenchmark.beanutilsBenchmark      avgt    6  0.885 ± 0.012  us/op
 * ConvertMapBenchmark.cglibBenchmark          avgt    6  0.060 ± 0.001  us/op
 * ConvertMapBenchmark.jacksonBenchmark        avgt    6  0.255 ± 0.007  us/op
 * ConvertMapBenchmark.jacksonToTreeBenchmark  avgt    6  0.243 ± 0.001  us/op
 * <p>
 * 针对cglib额外进行Map复制
 * Benchmark                                   Mode  Cnt  Score   Error  Units
 * ConvertMapBenchmark.beanutilsBenchmark      avgt    6  0.894 ± 0.024  us/op
 * ConvertMapBenchmark.cglibBenchmark          avgt    6  0.142 ± 0.012  us/op
 * ConvertMapBenchmark.jacksonBenchmark        avgt    6  0.260 ± 0.022  us/op
 * ConvertMapBenchmark.jacksonToTreeBenchmark  avgt    6  0.245 ± 0.009  us/op
 * <p>
 * Jackson转换map过程中直接copy数组、哈希表等可能会造成性能损耗
 * Beanutils执行结果类似于Cglib, 但是额外增加了class属性，估计底层直接使用了Java反射机制，不过它额外引入了commons-logging特别恶心
 * Cglib转换map树时直接复制子元素, 并且未处理子节点, 更加容易定制
 * <p>
 * 整体而言Cglib性能卓越、功能更加符合需求。
 *
 * @author sulin
 * @since 2019-05-07 19:24:41
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ConvertMapBenchmark {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static Bean bean = new Bean();

    @Test
    public void testJackson() {
        // Jackson转换map时候会完全复制List、Map等数据, 应该有办法优化它
        Map map = MAPPER.convertValue(bean, Map.class);
        System.out.println(map);
    }

    @Test
    public void testCglib() {
        // Cglib转换map树时直接复制子元素, 并且未处理子节点, 更加容易定制
        BeanMap map = BeanMap.create(bean);
        System.out.println(map);
    }

    @Test
    public void testBeanutils() throws Exception {
        // Beanutils执行结果类似于Cglib, 但是额外增加了class属性，估计底层直接使用了Java反射机制
        // 并且BeanUtils引入大量乱七八糟的日志
        Map<String, Object> map = PropertyUtils.describe(bean);
        System.out.println(map);
    }

    @Benchmark
    public void jacksonToTreeBenchmark() {
        MAPPER.valueToTree(bean.egg);
    }

    @Benchmark
    public void jacksonBenchmark() {
        MAPPER.convertValue(bean.egg, Map.class);
    }

    @Benchmark
    public void cglibBenchmark() {
        Map<String, Object> tmp = new TreeMap<>();
        for (PropertyDescriptor getter : ReflectUtils.getBeanGetters(bean.egg.getClass())) {
        }
        for (PropertyDescriptor property : ReflectUtils.getBeanProperties(bean.egg.getClass())) {
        }

        BeanMap map = BeanMap.create(bean.egg);
        for (Object key : map.keySet()) {
            tmp.put(key.toString(), map.get(key));
        }
    }

    @Benchmark
    public void beanutilsBenchmark() throws Exception {
        PropertyUtils.describe(bean.egg);
    }

    @Data
    public static class Bean {
        private int id = RandomUtils.nextInt();
        private String name = RandomStringUtils.randomAlphanumeric(16);
        private Date date = new Date();
        private Egg egg = new Egg();
        private List<String> tags = Arrays.asList("tag1", "tag2", "tag3", "tag4");
        private Map<String, Object> props = new HashMap<>();

        public Bean() {
            props.put("code", 123456);
            props.put("score", BigDecimal.valueOf(1000.33));
        }
    }

    @Data
    public static class Egg {
        private boolean b = true;
        private long timstamp = System.currentTimeMillis();
        private String desc = RandomStringUtils.randomAlphanumeric(10);
        private Date date = new Date();
    }

}
