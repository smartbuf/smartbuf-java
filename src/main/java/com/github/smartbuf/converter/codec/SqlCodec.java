package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.Codec;
import com.github.smartbuf.converter.Converter;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;

/**
 * Codec for `java.sql` package
 *
 * @author sulin
 * @since 2019-07-25 20:49:12
 */
public final class SqlCodec extends Codec {

    @Converter
    public Blob toBlob(byte[] bytes) throws SQLException {
        return new SerialBlob(bytes);
    }

    @Converter
    public InputStream toInputStream(Blob b) throws SQLException {
        return b.getBinaryStream();
    }

    @Converter
    public Clob toClob(String str) throws SQLException {
        return new SerialClob(str.toCharArray());
    }

    @Converter
    public Reader toReader(Clob c) throws SQLException {
        return c.getCharacterStream();
    }

    @Converter
    public Date toDate(Long time) {
        return new Date(time);
    }

    @Converter
    public Long toLong(Date date) {
        return date.getTime();
    }

    @Converter
    public Time toTime(Long time) {
        return new Time(time);
    }

    @Converter
    public Long toLong(Time time) {
        return time.getTime();
    }

    @Converter
    public Timestamp toTimestamp(Long time) {
        return new Timestamp(time);
    }

    @Converter
    public Long toLong(Timestamp t) {
        return t.getTime();
    }

}
