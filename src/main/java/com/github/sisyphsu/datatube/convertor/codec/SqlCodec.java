package com.github.sisyphsu.datatube.convertor.codec;

import com.github.sisyphsu.datatube.convertor.Codec;
import com.github.sisyphsu.datatube.convertor.Converter;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    public Date toDate(LocalDate d) {
        return Date.valueOf(d);
    }

    /**
     * Convert Date into LocalDate
     */
    @Converter
    public LocalDate toLocalDate(Date date) {
        return date.toLocalDate();
    }

    /**
     * Convert LocalTime to Time
     */
    @Converter
    public Time toTime(LocalTime time) {
        return Time.valueOf(time);
    }

    /**
     * Convert Time to String
     */
    @Converter
    public LocalTime toLocalTime(Time time) {
        return time.toLocalTime();
    }

    /**
     * Convert LocalDateTime to Timestamp
     */
    @Converter
    public Timestamp toTimestamp(LocalDateTime dt) {
        return Timestamp.valueOf(dt);
    }

    /**
     * Convert Timestamp to LocalDateTime
     */
    @Converter
    public LocalDateTime toLocalDateTime(Timestamp t) {
        return t.toLocalDateTime();
    }

}
