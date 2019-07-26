package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;

import java.math.BigInteger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Date's codec, don't support TimeZone
 *
 * @author sulin
 * @since 2019-05-13 18:05:26
 */
public class DateCodec extends Codec {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static final ZoneOffset DEFAULT = OffsetDateTime.now().getOffset();
    private static final BigInteger B = BigInteger.valueOf(1000000000);

    /**
     * Convert OffsetTime to LocalTime
     */
    @Converter
    public LocalTime toLocalTime(OffsetTime ot) {
        return ot.toLocalTime();
    }

    /**
     * Convert LocalTime to OffsetTime
     */
    @Converter
    protected OffsetTime toTargetNotNull(LocalTime localTime) {
        return localTime.atOffset(DEFAULT);
    }

    /**
     * Convert BigInteger to Duration
     */
    @Converter
    public Duration toDuration(BigInteger bigInteger) {
        BigInteger[] parts = bigInteger.divideAndRemainder(B);
        long seconds = parts[0].longValue();
        int ns = parts[1].intValue();
        return Duration.ofSeconds(seconds, ns);
    }

    /**
     * Convert Duration to BigInteger
     */
    @Converter
    public BigInteger toBigInteger(Duration duration) {
        BigInteger seconds = BigInteger.valueOf(duration.getSeconds());
        BigInteger nano = BigInteger.valueOf(duration.getNano());
        return seconds.multiply(B).add(nano);
    }

    /**
     * Convert Long to Date
     */
    @Converter
    public Date toDate(Long l) {
        return new Date(l);
    }

    /**
     * Convert Date to Long(Timestamp)
     */
    @Converter
    public Long toTimestamp(Date d) {
        return d.getTime();
    }

    /**
     * Convert Long to Calendar
     */
    @Converter
    public Calendar toCalendar(Long l) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(l);
        return c;
    }

    /**
     * Convert Calendar to Long
     */
    @Converter
    public Long toLong(Calendar c) {
        return c.getTimeInMillis();
    }

    /**
     * Convert String to ZoneId
     */
    @Converter
    public ZoneId toZoneId(String s) {
        return ZoneId.of(s);
    }

    /**
     * Convert ZoneId to String
     */
    @Converter
    public String toString(ZoneId zoneId) {
        return zoneId.getId();
    }

    /**
     * Convert ZonedDateTime to OffsetDateTime
     */
    @Converter
    public OffsetDateTime toOffsetDateTime(ZonedDateTime zdt) {
        return zdt.toOffsetDateTime();
    }

    /**
     * Convert OffsetDateTime to ZonedDateTime
     */
    @Converter
    public ZonedDateTime toZonedDateTime(OffsetDateTime odt) {
        return odt.toZonedDateTime();
    }

    /**
     * Convert Integer to Period
     */
    @Converter
    public Period toPeriod(Integer i) {
        int year = i / 10000;
        int month = (i % 10000) / 100;
        int day = i % 100;
        return Period.of(year, month, day);
    }

    /**
     * Convert Period to Integer
     */
    @Converter
    public Integer toInteger(Period period) {
        return period.getYears() * 10000 + period.getMonths() * 100 + period.getDays();
    }

    /**
     * Convert String to OffsetDateTime
     */
    @Converter
    public OffsetDateTime toOffsetDateTime(String s) {
        return OffsetDateTime.parse(s, FORMATTER);
    }

    /**
     * Convert OffsetDateTime to String
     */
    @Converter
    public String toString(OffsetDateTime odt) {
        return odt.format(FORMATTER);
    }

    /**
     * Convert Instant to Long
     */
    @Converter
    public Long toLong(Instant instant) {
        return instant.toEpochMilli();
    }

    /**
     * Convert Long to Instant
     */
    @Converter
    public Instant toInstant(Long l) {
        return Instant.ofEpochMilli(l);
    }

    /**
     * Convert LocalDateTime to LocalDate
     */
    @Converter
    public LocalDate toLocalDate(LocalDateTime ldt) {
        return ldt.toLocalDate();
    }

    /**
     * Convert LocalDate to LocalDateTime
     */
    @Converter
    public LocalDateTime toLong(LocalDate localDate) {
        return localDate.atStartOfDay();
    }


    /**
     * Convert ZonedDateTime to LocalDateTime
     */
    @Converter
    public LocalDateTime toLocalDateTime(OffsetDateTime zdt) {
        return zdt.toLocalDateTime();
    }

    /**
     * Convert LocalDateTime to ZonedDateTime
     */
    @Converter
    public OffsetDateTime toZonedDateTime(LocalDateTime ldt) {
        return ldt.atOffset(DEFAULT);
    }

    /**
     * Convert Long to OffsetTime
     */
    @Converter
    public OffsetTime toOffsetTime(Long l) {
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
     */
    @Converter
    public Long toLong(OffsetTime time) {
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
