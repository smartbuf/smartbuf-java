package com.github.sisyphsu.smartbuf.reflect;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.Date;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-29 21:18:15
 */
public class BeanReaderTest {

    @Test
    public void testPojo() {
        BeanReader reader = BeanReaderBuilder.build(Pojo.class);

        String[] names = reader.getFieldNames();
        assert names.length == 5;
        assert Objects.equals(names[0], "code");
        assert Objects.equals(names[1], "date");
        assert Objects.equals(names[2], "id");
        assert Objects.equals(names[3], "name");
        assert Objects.equals(names[4], "time");

        Pojo pojo = new Pojo();
        pojo.id = 10;
        pojo.name = "hello";
        pojo.code = 1000;
        pojo.time = System.currentTimeMillis();
        pojo.date = new Date();

        Object[] objects = reader.getValues(pojo);
        assert Objects.equals(objects[0], pojo.code);
        assert Objects.equals(objects[1], pojo.date);
        assert Objects.equals(objects[2], pojo.id);
        assert Objects.equals(objects[3], pojo.name);
        assert Objects.equals(objects[4], pojo.time);
    }

    @Test
    public void testStruct() {
        BeanReader reader = BeanReaderBuilder.build(Struct.class);

        String[] props = reader.getFieldNames();
        assert props.length == 7;
        assert Objects.equals(props[0], "code");
        assert Objects.equals(props[1], "date");
        assert Objects.equals(props[2], "flags");
        assert Objects.equals(props[3], "id");
        assert Objects.equals(props[4], "name");
        assert Objects.equals(props[5], "time");
        assert Objects.equals(props[6], "uuu");

        Struct struct = new Struct();
        struct.id = 10;
        struct.name = "hello";
        struct.setCode(1000);
        struct.setTime(System.currentTimeMillis());
        struct.setDate(new Date());
        struct.flags = BitSet.valueOf(new byte[]{1});
        struct.uuu = 1.0;

        Object[] objects = reader.getValues(struct);
        assert Objects.equals(objects[0], struct.getCode());
        assert Objects.equals(objects[1], struct.getDate());
        assert Objects.equals(objects[2], struct.flags);
        assert Objects.equals(objects[3], struct.getId());
        assert Objects.equals(objects[4], struct.getName());
        assert Objects.equals(objects[5], struct.getTime());
        assert Objects.equals(objects[6], struct.uuu);
    }

    @Test
    public void testHuge() {
        BeanReader reader = BeanReaderBuilder.build(Huge.class);

        Huge huge = new Huge();

        Object[] values = reader.getValues(huge);
        assert values.length == 136;
    }

    @Data
    public static class Pojo {
        private int     id;
        private String  name;
        private Integer code;
        private Long    time;
        private Date    date;

        private transient long xxxx;
    }

    public static class Struct extends Pojo {
        public int    id;
        public String name;

        public transient Boolean invalid;

        public static long timestamp = 0;

        public BitSet flags;
        public double uuu;

        private Boolean flag;

        public static void stuuu() {
        }

        public void isNull() {
        }

        public void setVisible(boolean visible) {
        }

        public boolean isVisible() {
            return true;
        }
    }

    @Data
    public static class Huge {
        private int    id;
        private String name;
        private Long   time;
        private byte   b;
        private short  s;
        private char   c;

        private long time0;
        private long time1;
        private long time2;
        private long time3;
        private long time4;
        private long time5;
        private long time6;
        private long time7;
        private long time8;
        private long time9;
        private long time10;
        private long time11;
        private long time12;
        private long time13;
        private long time14;
        private long time15;
        private long time16;
        private long time17;
        private long time18;
        private long time19;
        private long time20;
        private long time21;
        private long time22;
        private long time23;
        private long time24;
        private long time25;
        private long time26;
        private long time27;
        private long time28;
        private long time29;
        private long time30;
        private long time31;
        private long time32;
        private long time33;
        private long time34;
        private long time35;
        private long time36;
        private long time37;
        private long time38;
        private long time39;
        private long time40;
        private long time41;
        private long time42;
        private long time43;
        private long time44;
        private long time45;
        private long time46;
        private long time47;
        private long time48;
        private long time49;
        private long time50;
        private long time51;
        private long time52;
        private long time53;
        private long time54;
        private long time55;
        private long time56;
        private long time57;
        private long time58;
        private long time59;
        private long time60;
        private long time61;
        private long time62;
        private long time63;
        private long time64;
        private long time65;
        private long time66;
        private long time67;
        private long time68;
        private long time69;
        private long time70;
        private long time71;
        private long time72;
        private long time73;
        private long time74;
        private long time75;
        private long time76;
        private long time77;
        private long time78;
        private long time79;
        private long time80;
        private long time81;
        private long time82;
        private long time83;
        private long time84;
        private long time85;
        private long time86;
        private long time87;
        private long time88;
        private long time89;
        private long time90;
        private long time91;
        private long time92;
        private long time93;
        private long time94;
        private long time95;
        private long time96;
        private long time97;
        private long time98;
        private long time99;
        private long time100;
        private long time101;
        private long time102;
        private long time103;
        private long time104;
        private long time105;
        private long time106;
        private long time107;
        private long time108;
        private long time109;
        private long time110;
        private long time111;
        private long time112;
        private long time113;
        private long time114;
        private long time115;
        private long time116;
        private long time117;
        private long time118;
        private long time119;
        private long time120;
        private long time121;
        private long time122;
        private long time123;
        private long time124;
        private long time125;
        private long time126;
        private long time127;
        private long time128;
        private long time129;
    }

}
