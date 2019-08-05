package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;

/**
 * @author sulin
 * @since 2019-08-04 17:11:42
 */
public class JavaxCodecTest {

    private JavaxCodec codec = new JavaxCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        XMLGregorianCalendar calendar = XMLGregorianCalendarImpl.createDateTime(2019, 8, 4, 17, 14, 30);
        Calendar ca = codec.toCalendar(calendar);
        assert ca != null;
        assert ca.get(Calendar.YEAR) == calendar.getYear();
        assert ca.get(Calendar.MONTH) == calendar.getMonth() - 1; // Calendar's month from 0
        assert ca.get(Calendar.DAY_OF_MONTH) == calendar.getDay();
        assert ca.get(Calendar.HOUR_OF_DAY) == calendar.getHour();
        assert ca.get(Calendar.MINUTE) == calendar.getMinute();
        assert ca.get(Calendar.SECOND) == calendar.getSecond();
    }

}