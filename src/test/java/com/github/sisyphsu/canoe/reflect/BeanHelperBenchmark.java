package com.github.sisyphsu.canoe.reflect;

import lombok.Data;
import net.sf.cglib.beans.BeanMap;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Get BeanHelper by valueOf:
 * Benchmark                       Mode  Cnt    Score    Error  Units
 * BeanHelperBenchmark.accessor    avgt    6    8.076 ±  0.130  ns/op
 * BeanHelperBenchmark.beanHelper  avgt    6   27.146 ±  0.349  ns/op
 * BeanHelperBenchmark.beanmap     avgt    6  137.735 ± 13.050  ns/op
 * BeanHelperBenchmark.direct      avgt    6    8.362 ±  0.052  ns/op
 * BeanHelperBenchmark.methods     avgt    6   45.565 ±  0.708  ns/op
 * <p>
 * Cache BeanHelper(about 4~6ns for valueOf):
 * Benchmark                       Mode  Cnt    Score   Error  Units
 * BeanHelperBenchmark.accessor    avgt    6    8.010 ± 0.055  ns/op
 * BeanHelperBenchmark.beanHelper  avgt    6   21.527 ± 0.281  ns/op
 * BeanHelperBenchmark.beanmap     avgt    6  137.474 ± 0.644  ns/op
 * BeanHelperBenchmark.direct      avgt    6    8.261 ± 0.128  ns/op
 * BeanHelperBenchmark.methods     avgt    6   43.558 ± 0.405  ns/op
 * <p>
 * Where is the 20ns?
 *
 * @author sulin
 * @since 2019-10-28 17:57:59
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BeanHelperBenchmark {

    private static final User     USER     = new User();
    private static final Method[] GETTERS  = new Method[7];
    private static final Accessor ACCESSOR = BeanHelper.valueOf(User.class).accessor;

    static {
        try {
            GETTERS[0] = User.class.getDeclaredMethod("getId");
            GETTERS[1] = User.class.getDeclaredMethod("getBlocked");
            GETTERS[2] = User.class.getDeclaredMethod("getCreateTime");
            GETTERS[3] = User.class.getDeclaredMethod("getLoginTimes");
            GETTERS[4] = User.class.getDeclaredMethod("getNickname");
            GETTERS[5] = User.class.getDeclaredMethod("getPortrait");
            GETTERS[6] = User.class.getDeclaredMethod("getScore");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void beanmap() {
        BeanMap beanMap = BeanMap.create(USER);
        Object[] arr = new Object[beanMap.size()];
        int off = 0;
        for (Object key : beanMap.keySet()) {
            arr[off++] = beanMap.get(key);
        }
    }

    @Benchmark
    public void methods() throws Exception {
        Object[] arr = new Object[GETTERS.length];
        for (int i = 0; i < GETTERS.length; i++) {
            arr[i] = GETTERS[i].invoke(USER);
        }
    }

    @Benchmark
    public void beanHelper() {
        Object[] arr = BeanHelper.valueOf(User.class).getValues(USER);
    }

    @Benchmark
    public void accessor() {
        Object[] arr = new Object[GETTERS.length];
        ACCESSOR.getAll(USER, arr);
    }

    @Benchmark
    public void direct() {
        Object[] arr = new Object[GETTERS.length];
        arr[0] = USER.getId();
        arr[1] = USER.getBlocked();
        arr[2] = USER.getCreateTime();
        arr[3] = USER.getNickname();
        arr[4] = USER.getPortrait();
        arr[5] = USER.getLoginTimes();
        arr[6] = USER.getScore();
    }

    @Data
    public static class User {
        private long    id         = 1385434576456830976L;
        private Boolean blocked    = false;
        private String  nickname   = "1uOlvFfhZuda";
        private String  portrait   = "OCCCAcnrKtKmbOkzRNYbWLnR";
        private float   score      = 2.7177553E38f;
        private int     loginTimes = 1543;
        private Date    createTime = new Date();
    }

}
