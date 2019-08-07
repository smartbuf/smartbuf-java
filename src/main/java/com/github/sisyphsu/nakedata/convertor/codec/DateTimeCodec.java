package com.github.sisyphsu.nakedata.convertor.codec;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * "8 08 008 2008"   year
 * "3 03 Mar March"  month
 * "9 09 Sun Sunday" day
 * "4 04 16 16"      hour 12/24
 * "5 05"            minute
 * "7 07"            second
 * "1 12 123 1230"   sec.fraction
 * "1 12 123 123"    without zeroes
 * "P PM"            A.M. or P.M.
 * "-6 -06 -06:00"   time zone
 * <p>
 * <p>
 * 2018-03-31T01:23:45.678-0300
 * 2009-12-31 19:01:01 GMT-06:00
 * 2010-01-01 04:01:01GMT-04:00
 * yyyy/MM/dd HH:mm:ss
 * yyyy年M月d日 HH:mm:ss
 *
 * @author sulin
 * @since 2019-08-06 14:16:21
 */
public class DateTimeCodec {

    private static final Pattern DATETIME_PTN = Pattern.compile("(\\d+)\\D(\\d+)\\D(\\d+)[ T]");

    private static final String[] DATE_PTNS = {
            "yyyy-MM-dd",
            "dd-MM-yy",
            "dd-MM-yyyy",
            "yyyy/MM/dd",
            "dd/MM/yyyy",
            "dd/MM/yy",
            "dd.MM.yyyy",
            "yyyy年MM月dd日",
            "yyyy년M월d일",
            "yyyy년MM월dd일",
    };

    private static final String[] FORMATTERS = new String[]{
            "yyyy/MM/dd HH:mm:ss",
            "yyyy年M月d日 HH:mm:ss",
            "yyyy年M月d日 H时m分s秒",
            "yyyy년M월d일 HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",
            "dd/MM/yyyy HH:mm:ss",
            "dd.MM.yyyy HH:mm:ss",
            "dd-MM-yyyy HH:mm:ss",
            "yyyyMMdd",
            "yyyy/MM/dd",
            "yyyy年M月d日",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"
    };

    public static OffsetDateTime parseDateTime(String str) {
        int splitOff = -1;
        for (int i = 0; i < str.length() - 1; i++) {
            char curr = str.charAt(i);
            char next = str.charAt(i + 1);
            if ((curr == ' ' || next == 'T') && (next >= '0' && next <= '9')) {
                splitOff = i;
                break;
            }
        }
        LocalDate date = null;
        OffsetTime time = null;
        if (splitOff >= 0) {
            date = parseDate(str.substring(0, splitOff));
            time = parseTime(str.substring(splitOff + 2));
        }
        return null;
    }

    /**
     * Parse LocalDate like yyyyMMdd, yyyy-MM-dd, yyyy年MM月dd日 etc.
     */
    private static LocalDate parseDate(String str) {
        String parts[] = str.split("\\D");
        if (parts.length == 3) {
            int num1 = Integer.parseInt(parts[0]);
            int num2 = Integer.parseInt(parts[1]);
            int num3 = Integer.parseInt(parts[2]);
        }
        if (str.length() == 8) {

        }
        return null;
    }

    /**
     * Parse OffsetTime like hh:mm:ss.SSS, supports:
     * 155300
     * 155300+0500
     * 155300+05:00
     * 155300Z
     * 15
     * 15:53
     * 15:53:00
     * 15:53:00+05:00
     * 15:53:00.322348
     * 18:46:19Z
     * <p>
     * `Z` means UTC, pronounces “Zulu”.
     */
    public static OffsetTime parseTime(String str) {
        int offset = 0;
        return null;
    }

    private static class DateTimeParser {

        private final CharSequence src;
        private int offset;

        private ZoneOffset zoneOffset;

        private DateTimeParser(CharSequence src) {
            this.src = src;
        }

        /**
         * Parse ZoneOffset from:
         * Z
         * +05
         * +0530
         * +053000
         * +05:30
         * +05:30:00
         */
        private void parseOffset() {
            int hour = 0, minute = 0, second = 0;
            char first = nextChar();
            if (first == 'Z' || first == 'z') {
                if (this.hasMore()) {
                    throw error("'Z' TimeZone can't have suffix");
                }
            } else if (first == '+' || first == '-') {
                hour = this.nextInt(2);
                if (this.hasMore()) {

                }
            } else {
                throw error("TimeZone must be prefixed with +/-/Z/z");
            }
            this.zoneOffset = ZoneOffset.ofHoursMinutesSeconds(hour, minute, second);
        }

        private int nextInt(int limit) {
            Integer result = null;
            for (int i = 0; i < limit && offset < src.length(); i++) {
                this.expectMore();
                char c = nextChar();
                if (c < '0' || c > '9') {
                    break;
                }
                result = (c - '0') + (result == null ? 0 : result * 10);
                this.offset++;
            }
            if (result == null) {
                throw error("number expected");
            }
            return result;
        }

        private void expectMore() {
            if (offset < src.length()) {
                throw error("unexpected end");
            }
        }

        private char nextChar() {
            this.checkBounds();
            return src.charAt(offset);
        }

        private boolean hasMore() {
            return offset < src.length();
        }

        private DateTimeParseException error(String msg) {
            return new DateTimeParseException(msg, src, offset);
        }

        private void checkBounds() {
            if (offset >= src.length()) {
                throw new IndexOutOfBoundsException("offset > " + src.length());
            }
        }
    }

}
