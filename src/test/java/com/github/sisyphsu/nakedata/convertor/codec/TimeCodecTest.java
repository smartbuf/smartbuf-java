package com.github.sisyphsu.nakedata.convertor.codec;


import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
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
        assert date.equals(codec.toDate(codec.toLong(date)));

        Calendar calendar = Calendar.getInstance();
        Calendar newCalendar = codec.toCalendar(codec.toZonedDateTime(calendar));
        assert calendar.equals(newCalendar);

        ZoneId zoneId = ZoneId.systemDefault();
        assert zoneId.equals(codec.toZoneId(codec.toString(zoneId)));
    }

}