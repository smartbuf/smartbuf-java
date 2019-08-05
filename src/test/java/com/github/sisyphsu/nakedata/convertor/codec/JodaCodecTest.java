package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void test() {
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
//        DateTimeFormatter formatter = DateTimeFormat.fullDateTime();
//        String formatterStr = codec.toString(formatter);
//        System.out.println(formatterStr);
//        assert formatter.equals(codec.toDateTimeFormatter(formatterStr));

        // TODO date/time
    }

}