package com.github.smartbuf.converter.codec;

import com.github.sisyphsu.dateparser.DateParserUtils;
import com.github.smartbuf.converter.Codec;
import com.github.smartbuf.converter.Converter;

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
 * Long/Date could be converted to be Calendar through Instant ~ LocalDateTime ~ Calendar
 *
 * @author sulin
 * @since 2019-05-13 18:05:26
 */
public final class TimeCodec extends Codec {

    private static final SimpleDateFormat  DATE_FORMAT  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
    private static       DateTimeFormatter FORMATTER    = DateTimeFormatter.ISO_DATE_TIME;
    private static final ZoneOffset        LOCAL_OFFSET = ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset() / 1000);

    // ------------------ Date <==> String | Long

    @Converter
    public Date toDate(Long ms) {
        return new Date(ms);
    }

    @Converter
    public Date toDate(String str) {
        return DateParserUtils.parseDate(str);
    }

    @Converter
    public Long toLong(Date d) {
        return d.getTime();
    }

    @Converter(distance = 101)
    public String toString(Date d) {
        return DATE_FORMAT.format(d);
    }

    // ------------------- Calendar <==> String | Long

    @Converter
    public Calendar toCalendar(Long ms) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar;
    }

    @Converter
    public Calendar toCalendar(String str) {
        return DateParserUtils.parseCalendar(str);
    }

    @Converter
    public Long toLong(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    @Converter(distance = 101)
    public String toString(Calendar calendar) {
        return DATE_FORMAT.format(calendar.getTime());
    }

    // ------------------- ZoneId <==> String

    @Converter
    public ZoneId toZoneId(String s) {
        return ZoneId.of(s);
    }

    @Converter
    public String toString(ZoneId zoneId) {
        return zoneId.getId();
    }

    // ---------------------------------------- Instant

    @Converter
    public Instant toInstant(Long ms) {
        return Instant.ofEpochMilli(ms);
    }

    @Converter
    public Long toLong(Instant instant) {
        return instant.toEpochMilli();
    }

    @Converter
    public Instant toInstant(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC);
    }

    @Converter
    public LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), ZoneOffset.UTC);
    }

    // ---------------------------------------- Duration, could be converted to/from String and Instant

    @Converter
    public Duration toDuration(String str) {
        if (isDigist(str)) {
            return Duration.ofMillis(Long.parseLong(str)); // support timestamp string
        } else {
            return Duration.parse(str);
        }
    }

    @Converter
    public String toString(Duration duration) {
        return duration.toString();
    }

    @Converter
    public Duration toDuration(Instant instant) {
        return Duration.ofMillis(instant.toEpochMilli());
    }

    @Converter
    public Instant toInstant(Duration duration) {
        return Instant.ofEpochMilli(duration.toMillis());
    }

    // ---------------------------------------- Period, could be converted to/from String

    @Converter
    public Period toPeriod(String str) {
        return Period.parse(str);
    }

    @Converter
    public String toString(Period period) {
        return period.toString();
    }

    // ---------------------------------------- OffsetTime, LocalTime

    @Converter
    public LocalTime toLocalTime(OffsetTime ot) {
        long diffSeconds = LOCAL_OFFSET.getTotalSeconds() - ot.getOffset().getTotalSeconds();
        return LocalTime.ofNanoOfDay(ot.toLocalTime().toNanoOfDay() + diffSeconds * 1000000000);
    }

    @Converter
    public OffsetTime toOffsetTime(LocalTime localTime) {
        return localTime.atOffset(LOCAL_OFFSET);
    }

    @Converter
    public String toString(OffsetTime offsetTime) {
        return offsetTime.toString();
    }

    @Converter
    public OffsetTime toOffsetTime(String str) {
        OffsetDateTime dateTime = DateParserUtils.parseOffsetDateTime(str);
        return dateTime.toOffsetTime();
    }

    @Converter
    public OffsetTime toOffsetTime(Instant instant) {
        return OffsetTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Converter
    public Instant toInstant(OffsetTime offsetTime) {
        long second = offsetTime.toLocalTime().toSecondOfDay() - offsetTime.getOffset().getTotalSeconds();
        return Instant.ofEpochSecond(second, offsetTime.toLocalTime().getNano());
    }

    @Converter
    public OffsetTime toOffsetTime(OffsetDateTime odt) {
        return odt.toOffsetTime();
    }

    @Converter
    public OffsetDateTime toOffsetDateTime(OffsetTime ot) {
        return OffsetDateTime.of(1970, 1, 1, ot.getHour(), ot.getMinute(), ot.getSecond(), ot.getNano(), ot.getOffset());
    }

    // ---------------------------------------- LocalDate <==> LocalDateTime

    @Converter
    public LocalDate toLocalDate(LocalDateTime ldt) {
        return ldt.toLocalDate();
    }

    @Converter
    public LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    // ---------------------------------------- LocalDateTime <==> OffsetDateTime

    @Converter
    public LocalDateTime toLocalDateTime(OffsetDateTime zdt) {
        Instant instant = zdt.toInstant();
        return LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), LOCAL_OFFSET);
    }

    @Converter(distance = 10)
    public OffsetDateTime toOffsetDateTime(LocalDateTime ldt) {
        return ldt.atOffset(LOCAL_OFFSET);
    }

    // ---------------------------------------- ZonedDateTime <==> OffsetDateTime

    @Converter
    public ZonedDateTime toZonedDateTime(OffsetDateTime odt) {
        return odt.toZonedDateTime();
    }

    @Converter
    public OffsetDateTime toOffsetDateTime(ZonedDateTime zdt) {
        return zdt.toOffsetDateTime();
    }

    // ---------------------------------------- OffsetDateTime, could be converted to/from String and Instant etc.

    @Converter
    public String toString(OffsetDateTime odt) {
        return odt.format(FORMATTER);
    }

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
