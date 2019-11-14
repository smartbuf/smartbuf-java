package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.CodecFactory;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.TimeZone;

/**
 * @author sulin
 * @since 2019-08-04 17:20:28
 */
public class JodaCodecTest {

    private JodaCodec codec = new JodaCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testDateTime() {
        // Instant
        Instant instant = Instant.now();
        System.out.println(instant);
        System.out.println(codec.toInstant(instant));
        Instant newInstant = codec.toInstant(codec.toInstant(instant));
        System.out.println(newInstant);
        assert instant.toEpochMilli() == newInstant.toEpochMilli();

        // Duration
        Duration duration = Duration.ofMillis(System.currentTimeMillis());
        System.out.println(duration);
        System.out.println(codec.toDuration(duration));
        assert duration.equals(codec.toDuration(codec.toDuration(duration)));

        // LocalTime
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        System.out.println(codec.toLocalTime(localTime));
        assert localTime.equals(codec.toLocalTime(codec.toLocalTime(localTime)));

        // LocalDate
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        System.out.println(codec.toLocalDate(localDate));
        assert localDate.equals(codec.toLocalDate(codec.toLocalDate(localDate)));

        // DateTime
        DateTime dateTime = DateTime.now();
        System.out.println(dateTime);
        System.out.println(codec.toLocalDateTime(dateTime));
        assert dateTime.equals(codec.toDateTime(codec.toLocalDateTime(dateTime)));

        // LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        System.out.println(codec.toLocalDateTime(localDateTime));
        assert localDateTime.equals(codec.toLocalDateTime(codec.toLocalDateTime(localDateTime)));
    }

    @Test
    public void testOthers() {
        // interval
        Interval interval = new Interval(0, 365 * 24 * 60 * 60 * 1000L);
        String str = codec.toString(interval);
        System.out.println(str);
        assert interval.equals(codec.toInterval(str));

        // period
        Period period = new Period(2019, 8, 1, 4, 17, 30, 40, 300);
        String periodStr = codec.toString(period);
        System.out.println(periodStr);
        assert period.equals(codec.toPeriod(periodStr));

        // DateTimeFormatter
        assert codec.toDateTimeFormatter("yyyy-MM-dd") != null;
        try {
            codec.toString(DateTimeFormat.fullDateTime());
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        // DateTimeZone
        TimeZone zone = TimeZone.getDefault();
        TimeZone newZone = codec.toTimeZone(codec.toDateTimeZone(zone));
        System.out.println(zone);
        System.out.println(newZone);
        assert zone.getID().equals(newZone.getID());
    }

}
