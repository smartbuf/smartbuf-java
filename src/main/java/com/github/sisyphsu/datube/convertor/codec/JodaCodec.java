package com.github.sisyphsu.datube.convertor.codec;

import com.github.sisyphsu.datube.convertor.Codec;
import com.github.sisyphsu.datube.convertor.Converter;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.TimeZone;

/**
 * Codec for `org.joda.time` package.
 * <p>
 * https://blog.joda.org/2014/11/converting-from-joda-time-to-javatime.html
 *
 * @author sulin
 * @since 2019-07-26 11:56:20
 */
public final class JodaCodec extends Codec {

    /**
     * Convert java.LocalDateTime to joda.LocalDateTime
     */
    @Converter
    public LocalDateTime toLocalDateTime(java.time.LocalDateTime dt) {
        return new LocalDateTime(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(),
                dt.getHour(), dt.getMinute(), dt.getSecond(), dt.getNano() / 1000000);
    }

    /**
     * Convert joda.LocalDateTime to java.LocalDateTime
     */
    @Converter
    public java.time.LocalDateTime toLocalDateTime(LocalDateTime dt) {
        return java.time.LocalDateTime.of(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(),
                dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(), dt.getMillisOfSecond() * 1000000);
    }

    /**
     * Convert joda.LocalDateTime to joda.DateTime
     */
    @Converter
    public DateTime toDateTime(LocalDateTime ldt) {
        return ldt.toDateTime();
    }

    /**
     * Convert joda.DateTime to joda.LocalDateTime
     */
    @Converter
    public LocalDateTime toLocalDateTime(DateTime dt) {
        return dt.toLocalDateTime();
    }

    /**
     * Convert java.LocalDate to joda.LocalDate
     */
    @Converter
    public LocalDate toLocalDate(java.time.LocalDate ldt) {
        return new LocalDate(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth());
    }

    /**
     * Convert joda.LocalDate to java.LocalDate
     */
    @Converter
    public java.time.LocalDate toLocalDate(LocalDate date) {
        return java.time.LocalDate.of(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
    }

    /**
     * Convert java.LocalTimee to joda.LocalTime
     */
    @Converter
    public LocalTime toLocalTime(java.time.LocalTime time) {
        return LocalTime.fromMillisOfDay(time.toNanoOfDay() / 1000000L);
    }

    /**
     * Convert joda.LocalTime to java.LocalTime
     */
    @Converter
    public java.time.LocalTime toLocalTime(LocalTime time) {
        return java.time.LocalTime.ofNanoOfDay(time.getMillisOfDay() * 1000000L);
    }

    /**
     * Convert java.Duration to joda.Duration
     */
    @Converter
    public Duration toDuration(java.time.Duration d) {
        return Duration.millis(d.toMillis());
    }

    /**
     * Convert joda.Duration to java.Duration
     */
    @Converter
    public java.time.Duration toDuration(Duration d) {
        return java.time.Duration.ofMillis(d.getMillis());
    }

    /**
     * Convert java.Instant to joda.Instant
     */
    @Converter
    public Instant toInstant(java.time.Instant instant) {
        return Instant.ofEpochMilli(instant.toEpochMilli());
    }

    /**
     * Convert joda.Instant to java.Instant
     */
    @Converter
    public java.time.Instant toInstant(Instant instant) {
        return java.time.Instant.ofEpochMilli(instant.getMillis());
    }

    /**
     * Convert joda.DateTimeZone to java.TimeZone
     */
    @Converter
    public TimeZone toTimeZone(DateTimeZone zone) {
        return zone.toTimeZone();
    }

    /**
     * Convert java.TimeZone to joda.DateTimeZone
     */
    @Converter
    public DateTimeZone toDateTimeZone(TimeZone zone) {
        return DateTimeZone.forTimeZone(zone);
    }

    /**
     * Convert DateTimeFormatter to String
     */
    @Converter
    public String toString(DateTimeFormatter formatter) {
        throw new UnsupportedOperationException();
    }

    /**
     * Convert String to DateTimeFormatter
     */
    @Converter
    public DateTimeFormatter toDateTimeFormatter(String str) {
        return DateTimeFormat.forPattern(str);
    }

    /**
     * Convert String to Period
     */
    @Converter
    public Period toPeriod(String str) {
        return Period.parse(str);
    }

    /**
     * Convert Period to String
     */
    @Converter
    public String toString(Period period) {
        return period.toString();
    }

    /**
     * Convert String to Interal
     */
    @Converter
    public Interval toInterval(String str) {
        return Interval.parse(str);
    }

    /**
     * Convert Interal to String
     */
    @Converter
    public String toString(Interval interval) {
        return interval.toString();
    }

}
