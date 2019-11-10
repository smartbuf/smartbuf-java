package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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
        XMLGregorianCalendarImpl calendar = new XMLGregorianCalendarImpl();
        Calendar ca = codec.toCalendar(calendar);
        assert ca != null;
        assert ca.get(Calendar.YEAR) == calendar.getYear();
        assert ca.get(Calendar.MONTH) == calendar.getMonth();
        assert ca.get(Calendar.DAY_OF_MONTH) == calendar.getDay();
        assert ca.get(Calendar.HOUR_OF_DAY) == calendar.getHour();
        assert ca.get(Calendar.MINUTE) == calendar.getMinute();
        assert ca.get(Calendar.SECOND) == calendar.getSecond();
    }

    static class XMLGregorianCalendarImpl extends XMLGregorianCalendar {
        final GregorianCalendar calendar = new GregorianCalendar();

        @Override
        public void clear() {
        }

        @Override
        public void reset() {

        }

        @Override
        public void setYear(BigInteger year) {

        }

        @Override
        public void setYear(int year) {

        }

        @Override
        public void setMonth(int month) {

        }

        @Override
        public void setDay(int day) {

        }

        @Override
        public void setTimezone(int offset) {

        }

        @Override
        public void setHour(int hour) {

        }

        @Override
        public void setMinute(int minute) {

        }

        @Override
        public void setSecond(int second) {

        }

        @Override
        public void setMillisecond(int millisecond) {

        }

        @Override
        public void setFractionalSecond(BigDecimal fractional) {

        }

        @Override
        public BigInteger getEon() {
            return null;
        }

        @Override
        public int getYear() {
            return calendar.get(Calendar.YEAR);
        }

        @Override
        public BigInteger getEonAndYear() {
            return null;
        }

        @Override
        public int getMonth() {
            return calendar.get(Calendar.MONTH);
        }

        @Override
        public int getDay() {
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public int getTimezone() {
            return 0;
        }

        @Override
        public int getHour() {
            return calendar.get(Calendar.HOUR_OF_DAY);
        }

        @Override
        public int getMinute() {
            return calendar.get(Calendar.MINUTE);
        }

        @Override
        public int getSecond() {
            return calendar.get(Calendar.SECOND);
        }

        @Override
        public BigDecimal getFractionalSecond() {
            return null;
        }

        @Override
        public int compare(XMLGregorianCalendar xmlGregorianCalendar) {
            return 0;
        }

        @Override
        public XMLGregorianCalendar normalize() {
            return null;
        }

        @Override
        public String toXMLFormat() {
            return null;
        }

        @Override
        public QName getXMLSchemaType() {
            return null;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public void add(Duration duration) {

        }

        @Override
        public GregorianCalendar toGregorianCalendar() {
            return calendar;
        }

        @Override
        public GregorianCalendar toGregorianCalendar(TimeZone timezone, Locale aLocale, XMLGregorianCalendar defaults) {
            return calendar;
        }

        @Override
        public TimeZone getTimeZone(int defaultZoneoffset) {
            return null;
        }

        @Override
        public Object clone() {
            return null;
        }
    }

}
