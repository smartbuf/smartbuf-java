package com.github.sisyphsu.canoe.converter.codec;

import com.github.sisyphsu.dateparser.DateParserUtils;
import com.github.sisyphsu.canoe.converter.Codec;
import com.github.sisyphsu.canoe.converter.Converter;

import java.text.SimpleDateFormat;
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
public final class TimeCodec extends Codec {

    private static final SimpleDateFormat  DATE_FORMAT  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
    private static       DateTimeFormatter FORMATTER    = DateTimeFormatter.ISO_DATE_TIME;
    private static final ZoneOffset        LOCAL_OFFSET = ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset() / 1000);

    // ------------------ Date <==> String | Long

    /**
     * Convert Long to Date
     */
    @Converter
    public Date toDate(Long ms) {
        return new Date(ms);
    }

    /**
     * Convert String to Date
     */
    @Converter
    public Date toDate(String str) {
        return DateParserUtils.parseDate(str);
    }

    /**
     * Convert Date to Long(Timestamp)
     */
    @Converter
    public Long toLong(Date d) {
        return d.getTime();
    }

    /**
     * Convert Date to String
     */
    @Converter(distance = 500)
    public String toString(Date d) {
        return DATE_FORMAT.format(d);
    }

    // ------------------- Calendar <==> String | Long

    /**
     * Convert Long to Calendar
     */
    @Converter
    public Calendar toCalendar(Long ms) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar;
    }

    /**
     * Convert String to Calendar
     */
    @Converter
    public Calendar toCalendar(String str) {
        return DateParserUtils.parseCalendar(str);
    }

    /**
     * Converter Calendar to ZonedDateTime
     */
    @Converter
    public Long toLong(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    /**
     * Convert Calendar to String
     */
    @Converter
    public String toString(Calendar calendar) {
        return DATE_FORMAT.format(calendar.getTime());
    }

    // ------------------- ZoneId <==> String

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
     * Convert Long to Instant
     */
    @Converter
    public Instant toInstant(Long ms) {
        return Instant.ofEpochMilli(ms);
    }

    /**
     * Convert Instant to Long
     */
    @Converter
    public Long toLong(Instant instant) {
        return instant.toEpochMilli();
    }

    /**
     * Convert LocalDateTime to Instant
     */
    @Converter
    public Instant toInstant(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC);
    }

    /**
     * Convert Instant to LocalDateTime
     */
    @Converter
    public LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), ZoneOffset.UTC);
    }

    // ---------------------------------------- Duration, could be converted to/from String and Instant

    /**
     * Convert BigInteger to Duration
     */
    @Converter
    public Duration toDuration(String str) {
        if (isDigist(str)) {
            return Duration.ofMillis(Long.parseLong(str)); // support timestamp string
        } else {
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

    // ---------------------------------------- Period, could be converted to/from String

    /**
     * Convert String to Period, support timestamp
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

    // ---------------------------------------- OffsetTime, LocalTime

    /**
     * Convert OffsetTime to LocalTime
     */
    @Converter
    public LocalTime toLocalTime(OffsetTime ot) {
        long diffSeconds = LOCAL_OFFSET.getTotalSeconds() - ot.getOffset().getTotalSeconds();
        return LocalTime.ofNanoOfDay(ot.toLocalTime().toNanoOfDay() + diffSeconds * 1000000000);
    }

    /**
     * Convert LocalTime to OffsetTime
     */
    @Converter
    public OffsetTime toOffsetTime(LocalTime localTime) {
        return localTime.atOffset(LOCAL_OFFSET);
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
        OffsetDateTime dateTime = DateParserUtils.parseOffsetDateTime(str);
        return dateTime.toOffsetTime();
    }

    /**
     * Convert Instant to OffsetTime
     */
    @Converter
    public OffsetTime toOffsetTime(Instant instant) {
        return OffsetTime.ofInstant(instant, ZoneOffset.UTC);
    }

    /**
     * Convert OffsetTime to Instant
     */
    @Converter
    public Instant toInstant(OffsetTime offsetTime) {
        long second = offsetTime.toLocalTime().toSecondOfDay() - offsetTime.getOffset().getTotalSeconds();
        return Instant.ofEpochSecond(second, offsetTime.toLocalTime().getNano());
    }

    /**
     * Convert OffsetDateTime to OffsetTime
     */
    @Converter
    public OffsetTime toOffsetTime(OffsetDateTime odt) {
        return odt.toOffsetTime();
    }

    /**
     * Convert OffsetTime to OffsetDateTime
     */
    @Converter
    public OffsetDateTime toOffsetDateTime(OffsetTime ot) {
        return OffsetDateTime.of(1970, 1, 1, ot.getHour(), ot.getMinute(), ot.getSecond(), ot.getNano(), ot.getOffset());
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

    // ---------------------------------------- LocalDateTime <==> OffsetDateTime

    /**
     * Convert ZonedDateTime to LocalDateTime
     */
    @Converter
    public LocalDateTime toLocalDateTime(OffsetDateTime zdt) {
        Instant instant = zdt.toInstant();
        return LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), LOCAL_OFFSET);
    }

    /**
     * Convert LocalDateTime to ZonedDateTime
     */
    @Converter
    public OffsetDateTime toOffsetDateTime(LocalDateTime ldt) {
        return ldt.atOffset(LOCAL_OFFSET);
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
     * Convert OffsetDateTime to String
     */
    @Converter
    public String toString(OffsetDateTime odt) {
        return odt.format(FORMATTER);
    }

    /**
     * Convert String to OffsetDateTime
     */
    @Converter
    public OffsetDateTime toOffsetDateTime(String s) {
        return DateParserUtils.parseOffsetDateTime(s);
    }

    private boolean isDigist(String str) {
        boolean allDigit = str.length() > 0;
        for (int i = 0; allDigit && i < str.length(); i++) {
            char ch = str.charAt(i);
            allDigit = ch >= '0' && ch <= '9';
        }
        return allDigit;
    }

}
