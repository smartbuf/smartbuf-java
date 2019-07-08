package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.OffsetTime;
import java.time.ZoneOffset;

/**
 * OffsetTime's codec
 *
 * @author sulin
 * @since 2019-05-10 10:45:58
 */
public class OffsetTimeCodec extends Codec {

    /**
     * Convert Long to OffsetTime
     *
     * @param l Long
     * @return OffsetTime
     */
    public OffsetTime toOffsetTime(Long l) {
        if (l == null)
            return null;
        int zone = (int) (l / Math.abs(l) * l % 1000);
        l = Math.abs(l) / 1000;
        int second = (int) (l % 100000);
        l = l / 100000;
        int ms = (int) (l % 1000);
        l = l / 1000;
        int us = (int) (l % 1000);
        l = l / 1000;
        int ns = (int) (l % 1000);
        second = second % 60;
        ns = ms * 1000000 + us * 1000 + ns;
        int hour = second / 3600;
        int minute = (second / 60) % 60;
        ZoneOffset offset = ZoneOffset.ofHoursMinutes(zone / 60, Math.abs(zone % 60));
        return OffsetTime.of(hour, minute, second, ns, offset);
    }

    /**
     * Convert OffsetTime to Long with specified format
     *
     * @param time OffsetTime
     * @return Long
     */
    public Long toLong(OffsetTime time) {
        if (time == null)
            return null;
        long zone = time.getOffset().getTotalSeconds() / 60; // ±(12 * 60)
        long second = (time.getHour() * 60 + time.getMinute()) * 60 + time.getSecond(); // 24 * 60 * 60
        long ms = time.getNano() / 1000000;
        long us = (time.getNano() / 1000) % 1000;
        long ns = time.getNano() % 1000;

        long result = ns;
        result = result * 1000 + ns;
        result = result * 1000 + us;
        result = result * 1000 + ms;
        result = result * 100000 + second;
        result = result * 1000 + zone;
        return result;
    }

}
