package com.github.sisyphsu.nakedata.utils;

/**
 * @author sulin
 * @since 2019-04-27 16:03:53
 */
public class NumberUtils {

    private NumberUtils() {
    }

    public static long intToUint(long l) {
        return (l << 1) ^ (l >> 63);
    }

    public static long uintToInt(long l) {
        return (l >>> 1) ^ -(l & 1);
    }

    public static int floatToBits(float f) {
        return Float.floatToRawIntBits(f);
    }

    public static float bitsToFloat(int i) {
        return Float.intBitsToFloat(i);
    }

    public static long doubleToBits(double d) {
        return Double.doubleToRawLongBits(d);
    }

    public static double bitsToDouble(long l) {
        return Double.longBitsToDouble(l);
    }

}
