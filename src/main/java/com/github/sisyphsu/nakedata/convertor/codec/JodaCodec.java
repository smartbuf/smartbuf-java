package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
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
public class JodaCodec extends Codec {

    // -------- LocalDateTime

    @Converter
    public LocalDateTime toLocalDateTime(java.time.LocalDateTime dt) {
        return new LocalDateTime(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(),
                dt.getHour(), dt.getMinute(), dt.getSecond());
    }

    @Converter
    public java.time.LocalDateTime toZonedDateTime(LocalDateTime dt) {
        return java.time.LocalDateTime.of(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(),
                dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute());
    }

    // -------- DateTime <==> LocalDateTime

    @Converter
    public DateTime toDateTime(LocalDateTime ldt) {
        return ldt.toDateTime();
    }

    @Converter
    public LocalDateTime toOffsetDateTime(DateTime dt) {
        return dt.toLocalDateTime();
    }

    // --------- LocalDate <==> java.time.LocalDate

    @Converter
    public LocalDate toLocalDate(java.time.LocalDate ldt) {
        return new LocalDate(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth());
    }

    @Converter
    public java.time.LocalDate toLocalDate(LocalDate date) {
        return java.time.LocalDate.of(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
    }

    // -------- LocalTime <==> java.time.LocalTime

    @Converter
    public LocalTime toLocalTime(java.time.LocalTime time) {
        return LocalTime.fromMillisOfDay(time.toNanoOfDay() / 1000000L);
    }

    @Converter
    public java.time.LocalTime toLocalTime(LocalTime time) {
        return java.time.LocalTime.ofNanoOfDay(time.getMillisOfDay() * 1000000L);
    }

    // ---------------- Duration <==> java.time.Duration

    @Converter
    public Duration toDuration(java.time.Duration d) {
        return Duration.millis(d.toMillis());
    }

    @Converter
    public java.time.Duration toDuration(Duration d) {
        return java.time.Duration.ofMillis(d.getMillis());
    }

    // ---------------- Instant <==> java.time.Instant

    @Converter
    public Instant toInstant(java.time.Instant instant) {
        return Instant.ofEpochMilli(instant.toEpochMilli());
    }

    @Converter
    public java.time.Instant toInstant(Instant instant) {
        return java.time.Instant.ofEpochMilli(instant.getMillis());
    }

    // ---------------- DateTimeZone

    @Converter
    public TimeZone toTimeZone(DateTimeZone zone) {
        return zone.toTimeZone();
    }

    @Converter
    public DateTimeZone toDateTimeZone(TimeZone zone) {
        return DateTimeZone.forTimeZone(zone);
    }

    // ---------------- DateTimeFormatter

    @Converter
    public String toString(DateTimeFormatter formatter) {
        return formatter.toString();
    }

    @Converter
    public DateTimeFormatter toDateTimeFormatter(String str) {
        return DateTimeFormat.forPattern(str);
    }

    // ---------------- Period <==> String

    @Converter
    public Period toPeriod(String str) {
        return Period.parse(str);
    }

    @Converter
    public String toString(Period period) {
        return period.toString();
    }

    // --------------- Interval <==> String

    @Converter
    public Interval toInterval(String str) {
        return Interval.parse(str);
    }

    @Converter
    public String toString(Interval interval) {
        return interval.toString();
    }

}
