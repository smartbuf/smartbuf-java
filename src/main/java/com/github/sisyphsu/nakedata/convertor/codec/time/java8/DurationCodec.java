package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.math.BigInteger;
import java.time.Duration;

/**
 * Duration's codec
 *
 * @author sulin
 * @since 2019-05-10 10:46:34
 */
public class DurationCodec extends Codec {

    private static final BigInteger B = BigInteger.valueOf(1000000000);

    /**
     * Convert BigInteger to Duration
     *
     * @param bigInteger BigInteger
     * @return Duration
     */
    @Converter
    public Duration toDuration(BigInteger bigInteger) {
        if (bigInteger == null)
            return null;

        BigInteger[] parts = bigInteger.divideAndRemainder(B);
        long seconds = parts[0].longValue();
        int ns = parts[1].intValue();
        return Duration.ofSeconds(seconds, ns);
    }

    /**
     * Convert Duration to BigInteger
     *
     * @param duration Duration
     * @return BigInteger
     */
    @Converter
    public BigInteger toBigInteger(Duration duration) {
        if (duration == null)
            return null;

        BigInteger seconds = BigInteger.valueOf(duration.getSeconds());
        BigInteger nano = BigInteger.valueOf(duration.getNano());
        return seconds.multiply(B).add(nano);
    }

}
