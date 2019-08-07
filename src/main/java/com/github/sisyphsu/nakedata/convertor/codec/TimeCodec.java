package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Time's codec, support Java8's new java.time package.
 * <p>
 * java.util.Date could be converted to and from long(timestamp),
 * it could also converted to and from LocalDateTime through Instant
 * <p>
 * java.util.Calendar could be converted to/from LocalDateTime
 * Long/Date could be converted to be Calendar through Instant -> LocalDateTime -> Calendar
 *
 * @author sulin
 * @since 2019-05-13 18:05:26
 */
public class TimeCodec extends Codec {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static final ZoneOffset DEFAULT = OffsetDateTime.now().getOffset();

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
    public Long toLong(Date d) {
        return d.getTime();
    }

    /**
     * Convert Long to Calendar
     */
    @Converter
    public Calendar toCalendar(ZonedDateTime dateTime) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(dateTime.getZone()));
        calendar.setTimeInMillis(dateTime.toInstant().toEpochMilli());
        return calendar;
    }

    /**
     * Converter Calendar to ZonedDateTime
     */
    @Converter
    public ZonedDateTime toZonedDateTime(Calendar calendar) {
        return ZonedDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
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

    // ---------------------------------------- Instant

    /**
     * Convert Instant to Long
     */
    @Converter
    public Date toLong(Instant instant) {
        return new Date(instant.toEpochMilli());
    }

    /**
     * Convert Long to Instant
     */
    @Converter
    public Instant toInstant(Date date) {
        return Instant.ofEpochMilli(date.getTime());
    }

    // ---------------------------------------- Duration, could be converted to/from String and Instant

    /**
     * Convert BigInteger to Duration
     */
    @Converter
    public Duration toDuration(String str) {
        try {
            return Duration.ofMillis(Long.parseLong(str)); // support timestamp string
        } catch (NumberFormatException e) {
            return Duration.parse(str);
        }
    }

    /**
     * Convert Duration to BigInteger
     */
    @Converter
    public String toString(Duration duration) {
        return duration.toString();
    }

    /**
     * Converter Instant to Duration
     */
    @Converter
    public Duration toDuration(Instant instant) {
        return Duration.ofMillis(instant.toEpochMilli());
    }

    /**
     * Converter Duration to Instant
     */
    @Converter
    public Instant toInstant(Duration duration) {
        return Instant.ofEpochMilli(duration.toMillis());
    }

    // ---------------------------------------- Period, could be converted to/from String and Duration

    /**
     * Convert String to Period, support timestamp
     */
    @Converter
    public Period toPeriod(String str) {
        try {
            return Period.from(Duration.ofMillis(Long.parseLong(str)));
        } catch (NumberFormatException e) {
            return Period.parse(str);
        }
    }

    /**
     * Convert Period to String
     */
    @Converter
    public String toString(Period period) {
        return period.toString();
    }

    /**
     * Convert Duration to Period
     */
    @Converter
    public Period toPeriod(Duration duration) {
        return Period.from(duration);
    }

    /**
     * Convert Period to Duration
     */
    @Converter
    public Duration toDuration(Period period) {
        return Duration.from(period);
    }

    // ---------------------------------------- OffsetTime, LocalTime

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
    public OffsetTime toOffsetTime(LocalTime localTime) {
        return localTime.atOffset(DEFAULT);
    }

    /**
     * Convert OffsetTime to String
     */
    @Converter
    public String toString(OffsetTime offsetTime) {
        return offsetTime.toString();
    }

    /**
     * Convert String to OffsetTime
     */
    @Converter
    public OffsetTime toOffsetTime(String str) {
        try {
            return OffsetTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(str)), DEFAULT);
        } catch (Exception e) {
            return OffsetTime.parse(str);
        }
    }

    /**
     * Convert Instant to OffsetTime
     */
    @Converter
    public OffsetTime toOffsetTime(Instant instant) {
        return OffsetTime.ofInstant(instant, DEFAULT);
    }

    /**
     * Convert OffsetTime to Instant
     */
    @Converter
    public Instant toInstant(OffsetTime offsetTime) {
        return Instant.from(offsetTime);
    }

    // ---------------------------------------- LocalDate <==> LocalDateTime

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
    public LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    // ---------------------------------------- LocalDateTime

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
    public OffsetDateTime toOffsetDateTime(LocalDateTime ldt) {
        return ldt.atOffset(DEFAULT);
    }

    // ---------------------------------------- ZonedDateTime <==> OffsetDateTime

    /**
     * Convert OffsetDateTime to ZonedDateTime
     */
    @Converter
    public ZonedDateTime toZonedDateTime(OffsetDateTime odt) {
        return odt.toZonedDateTime();
    }

    /**
     * Convert ZonedDateTime to OffsetDateTime
     */
    @Converter
    public OffsetDateTime toOffsetDateTime(ZonedDateTime zdt) {
        return zdt.toOffsetDateTime();
    }

    // ---------------------------------------- OffsetDateTime, could be converted to/from String and Instant etc.

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

}
