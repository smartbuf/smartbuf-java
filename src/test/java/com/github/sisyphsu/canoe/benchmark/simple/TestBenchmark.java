package com.github.sisyphsu.canoe.benchmark.simple;

import lombok.Data;
import net.sf.cglib.beans.BeanMap;
import org.openjdk.jmh.annotations.*;
import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark          Mode  Cnt     Score     Error  Units
 * TestBenchmark.bbb  avgt    6  5318.682 ± 115.968  ns/op
 * <p>
 * 3-fields:
 * TestBenchmark.convert  avgt    6  463.863 ± 38.506  ns/op
 * 2-fields:
 * TestBenchmark.convert  avgt    6  391.941 ± 9.256  ns/op
 * 1-fields:
 * TestBenchmark.convert  avgt    6  318.060 ± 19.271  ns/op
 * <p>
 * 70ns/1-fields, 250ns for BeanMap?
 * <p>
 * Use Method
 * Benchmark              Mode  Cnt   Score   Error  Units
 * TestBenchmark.beanmap  avgt    6  92.280 ± 6.384  ns/op
 * TestBenchmark.methods  avgt    6  17.710 ± 0.295  ns/op
 * Use MethodAccessor
 * Benchmark              Mode  Cnt   Score   Error  Units
 * TestBenchmark.beanmap  avgt    6  91.210 ± 6.268  ns/op
 * TestBenchmark.methods  avgt    6  15.117 ± 0.195  ns/op
 * <p>
 * Benchmark              Mode  Cnt   Score   Error  Units
 * TestBenchmark.asm      avgt    6   5.368 ± 0.137  ns/op
 * TestBenchmark.beanmap  avgt    6  91.468 ± 2.714  ns/op
 * TestBenchmark.direct   avgt    6   0.258 ± 0.001  ns/op
 * TestBenchmark.methods  avgt    6  12.011 ± 0.469  ns/op
 * <p>
 * ASM's getter proxy is faster, could use it.
 *
 * @author sulin
 * @since 2019-10-28 17:57:59
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class TestBenchmark {

    private static final UserModel USER = UserModel.random();

    static final Person           person  = new Person();
    static final MethodAccessor[] methods = new MethodAccessor[3];

    static Class<? extends AsmTest.BeanGetter>       cls;
    static Constructor<? extends AsmTest.BeanGetter> constructor;

    static {
        try {
            methods[0] = ReflectionFactory.getReflectionFactory().newMethodAccessor(Person.class.getDeclaredMethod("getId"));
            methods[1] = ReflectionFactory.getReflectionFactory().newMethodAccessor(Person.class.getDeclaredMethod("getName"));
            methods[2] = ReflectionFactory.getReflectionFactory().newMethodAccessor(Person.class.getDeclaredMethod("getTime"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            cls = AsmTest.createGetter(Person.class,
                Person.class.getDeclaredMethod("getId"),
                Person.class.getDeclaredMethod("getName"),
                Person.class.getDeclaredMethod("getTime"));
            constructor = cls.getDeclaredConstructor(Person.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void beanmap() {
        BeanMap beanMap = BeanMap.create(person);
        for (Object key : beanMap.keySet()) {
            beanMap.get(key);
        }
    }

    @Benchmark
    public void methods() throws Exception {
        for (MethodAccessor method : methods) {
            method.invoke(person, new Object[0]);
        }
    }

    @Benchmark
    public void asm() throws Exception {
        AsmTest.BeanGetter getter = constructor.newInstance(person);
        getter.getAll();
    }

    @Benchmark
    public void direct() {
        person.getId();
        person.getName();
        person.getTime();
    }

    @Data
    public static class Person {
        private Integer id   = 100;
        private String  name = "hello world";
        private Long    time = 1000000L;
    }

}
