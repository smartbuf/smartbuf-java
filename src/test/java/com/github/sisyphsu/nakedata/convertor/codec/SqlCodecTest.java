package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * @author sulin
 * @since 2019-08-04 18:58:39
 */
@SuppressWarnings("ALL")
public class SqlCodecTest {

    private SqlCodec codec = new SqlCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() throws SQLException {
        byte[] byets = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        Blob blob = codec.toBlob(byets);
        assert codec.toInputStream(blob) != null; // TODO InputStream => byte[]

        String str = "hello world";
        Clob clob = codec.toClob(str);
        assert codec.toReader(clob) != null; // TODO Reader => String

        Date date = new Date(System.currentTimeMillis());
        Date date2 = codec.toDate(codec.toLocalDate(date));
        assert date2.getYear() == date.getYear();
        assert date2.getMonth() == date.getMonth();
        assert date2.getDay() == date.getDay();

        Time time = new Time(System.currentTimeMillis());
        Time time2 = codec.toTime(codec.toLocalTime(time));
        assert time.getHours() == time2.getHours();
        assert time.getMinutes() == time2.getMinutes();
        assert time.getSeconds() == time2.getSeconds();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        assert timestamp.equals(codec.toTimestamp(codec.toLocalDateTime(timestamp)));
    }

}