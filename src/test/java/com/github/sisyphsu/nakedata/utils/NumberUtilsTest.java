package com.github.sisyphsu.nakedata.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

/**
 * @author sulin
 * @since 2019-04-27 16:14:07
 */
@Slf4j
public class NumberUtilsTest {

    @Test
    public void testFloat() {
        float f = 1.1f;
        log.info("{}: \t{}", f, NumberUtils.floatToBits(f));

        for (int i = 0; i < 1000000; i++) {
            float f1 = RandomUtils.nextFloat();
            int bits = NumberUtils.floatToBits(f1);
            float f2 = NumberUtils.bitsToFloat(bits);
            if (f1 != f2) {
                log.error("err: {}, {}", f1, f2);
            }
        }
    }

    @Test
    public void testDouble() {
        double d = 1.1;
        log.info("{}: \t{}", d, NumberUtils.doubleToBits(d));

        for (int i = 0; i < 1000000; i++) {
            double d1 = RandomUtils.nextDouble();
            long bits = NumberUtils.doubleToBits(d1);
            double d2 = NumberUtils.bitsToDouble(bits);
            if (d1 != d2) {
                log.error("err: {}, {}", d1, d2);
            }
        }
    }

}