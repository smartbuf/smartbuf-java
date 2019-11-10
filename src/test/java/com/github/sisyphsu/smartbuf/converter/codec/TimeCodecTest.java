package com.github.sisyphsu.smartbuf.converter.codec;


import com.github.sisyphsu.smartbuf.converter.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author sulin
 * @since 2019-08-04 17:02:14
 */
public class TimeCodecTest {

    private TimeCodec codec = new TimeCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testJdk6() {
        Date date = new Date();
        Date date1 = codec.toDate(codec.toLong(date));
        Date date2 = codec.toDate(codec.toString(date));
        assert date.equals(date1);
        assert date.equals(date2);

        Calendar calendar = Calendar.getInstance();
        String str = codec.toString(calendar);
        Calendar calendar1 = codec.toCalendar(codec.toLong(calendar));
        Calendar calendar2 = codec.toCalendar(str);
        assert calendar.getTime().equals(calendar1.getTime());
        assert calendar.getTime().equals(calendar2.getTime());
    }

    @Test
    public void testJdk8() {
        ZoneId zoneId = ZoneId.systemDefault();
        assert zoneId.equals(codec.toZoneId(codec.toString(zoneId)));

        long ms = System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.now();
        Instant instant = codec.toInstant(dateTime);

        assert dateTime.equals(codec.toLocalDateTime(instant));
        assert ms == codec.toLong(codec.toInstant(ms));

        Duration duration = Duration.ofMillis(ms);
        assert duration.equals(codec.toDuration(String.valueOf(ms)));
        assert duration.equals(codec.toDuration(codec.toString(duration)));
        assert duration.equals(codec.toDuration(codec.toInstant(duration)));
        try {
            codec.toDuration("");
            assert false;
        } catch (Exception e) {
            assert e instanceof DateTimeParseException;
        }
        try {
            codec.toDuration(".111");
            assert false;
        } catch (Exception e) {
            assert e instanceof DateTimeParseException;
        }

        Period period = Period.of(10, 10, 20);
        assert period.equals(codec.toPeriod(codec.toString(period)));
    }

    @Test
    public void testTime() {
        String str = "10:20:30.66666666-04:00";
        OffsetTime offsetTime = codec.toOffsetTime(str);
        assert offsetTime.getHour() == 10;
        assert offsetTime.getNano() == 666666660;
        assert offsetTime.getOffset().equals(ZoneOffset.of("-0400"));
        assert offsetTime.equals(codec.toOffsetTime(codec.toString(offsetTime)));

        System.out.println(offsetTime);
        assert offsetTime.equals(codec.toOffsetTime(codec.toOffsetDateTime(offsetTime)));

        LocalTime localTime = codec.toLocalTime(offsetTime);
        assert localTime.equals(codec.toLocalTime(codec.toOffsetTime(localTime)));
        System.out.println(localTime);

        Instant instant = codec.toInstant(codec.toOffsetTime(localTime));
        assert localTime.equals(codec.toLocalTime(codec.toOffsetTime(instant)));
        System.out.println(instant);
    }

    @Test
    public void testDate() {
        LocalDate date = LocalDate.now();
        assert date.equals(codec.toLocalDate(codec.toLocalDateTime(date)));

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        OffsetDateTime offsetDateTime = codec.toOffsetDateTime(localDateTime);
        System.out.println(offsetDateTime);
        assert localDateTime.equals(codec.toLocalDateTime(offsetDateTime));

        ZonedDateTime zonedDateTime = codec.toZonedDateTime(offsetDateTime);
        System.out.println(zonedDateTime);
        assert offsetDateTime.equals(codec.toOffsetDateTime(zonedDateTime));

        assert offsetDateTime.equals(codec.toOffsetDateTime(codec.toString(offsetDateTime)));

        String str = "2019/10/10 10:30:50.12345678 +10:00";
        OffsetDateTime dateTime = codec.toOffsetDateTime(str);
        assert dateTime.getOffset().getId().equals("+10:00");
        assert dateTime.getYear() == 2019;
        assert dateTime.getMonth() == Month.OCTOBER;
        assert dateTime.getDayOfMonth() == 10;
        assert dateTime.getHour() == 10;
        assert dateTime.getMinute() == 30;
        assert dateTime.getSecond() == 50;
        assert dateTime.getNano() == 123456780;
    }

}
