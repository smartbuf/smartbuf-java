package com.github.sisyphsu.nakedata.convertor.adaptor.time.joda;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.math.BigInteger;
import java.time.Duration;

/**
 * {@link Duration}与{@link BigInteger}的数据转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:34
 */
public class DurationAdaptor extends Adaptor<Duration, BigInteger> {

    private static final BigInteger B = BigInteger.valueOf(1000000000);

    @Override
    protected BigInteger toTargetNotNull(Duration duration) {
        BigInteger seconds = BigInteger.valueOf(duration.getSeconds());
        BigInteger nano = BigInteger.valueOf(duration.getNano());
        return seconds.multiply(B).add(nano);
    }

    @Override
    protected Duration toSourceNotNull(BigInteger bigInteger) {
        BigInteger[] parts = bigInteger.divideAndRemainder(B);
        long seconds = parts[0].longValue();
        int ns = parts[1].intValue();
        return Duration.ofSeconds(seconds, ns);
    }

}
