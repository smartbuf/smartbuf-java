package com.github.sisyphsu.datube.convertor.codec;

import com.github.sisyphsu.datube.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.Arrays;

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
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        Blob blob = codec.toBlob(bytes);
        InputStream is = codec.toInputStream(blob);
        assert Arrays.equals(codec.convert(is, byte[].class), bytes);

        String str = "hello world";
        Clob clob = codec.toClob(str);
        Reader reader = codec.toReader(clob);
        assert str.equals(codec.convert(reader, String.class));

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
