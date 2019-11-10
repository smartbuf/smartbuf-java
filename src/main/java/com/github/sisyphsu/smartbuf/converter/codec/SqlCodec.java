package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.Codec;
import com.github.sisyphsu.smartbuf.converter.Converter;

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

    /**
     * Convert byte[] to Blob
     */
    @Converter
    public Blob toBlob(byte[] bytes) throws SQLException {
        return new SerialBlob(bytes);
    }

    /**
     * Convert Blob to InputStream
     */
    @Converter
    public InputStream toInputStream(Blob b) throws SQLException {
        return b.getBinaryStream();
    }

    /**
     * Convert String to Clob
     */
    @Converter
    public Clob toClob(String str) throws SQLException {
        return new SerialClob(str.toCharArray());
    }

    /**
     * Convert Clob to Reader
     */
    @Converter
    public Reader toReader(Clob c) throws SQLException {
        return c.getCharacterStream();
    }

    /**
     * Convert LocalDate to Date
     */
    @Converter
    public Date toDate(Long time) {
        return new Date(time);
    }

    /**
     * Convert Date into LocalDate
     */
    @Converter
    public Long toLong(Date date) {
        return date.getTime();
    }

    /**
     * Convert LocalTime to Time
     */
    @Converter
    public Time toTime(Long time) {
        return new Time(time);
    }

    /**
     * Convert Time to String
     */
    @Converter
    public Long toLong(Time time) {
        return time.getTime();
    }

    /**
     * Convert LocalDateTime to Timestamp
     */
    @Converter
    public Timestamp toTimestamp(Long time) {
        return new Timestamp(time);
    }

    /**
     * Convert Timestamp to LocalDateTime
     */
    @Converter
    public Long toLong(Timestamp t) {
        return t.getTime();
    }

}
