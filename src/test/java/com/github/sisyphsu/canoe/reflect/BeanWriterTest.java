package com.github.sisyphsu.canoe.reflect;

import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-11-09 16:35:00
 */
public class BeanWriterTest {

    @Test
    public void test() {
        BeanWriterBuilder.build(Pojo1.class);
        BeanWriter writer = BeanWriterBuilder.build(Pojo1.class);
        assert writer.getFields().length == 4;

        BeanWriterBuilder.build(Pojo2.class);
        writer = BeanWriterBuilder.build(Pojo2.class);
        assert writer.getFields().length == 2;
    }

    @Test
    public void testError() {
        String tmp = BeanWriter.API_NAME;
        BeanWriter.API_NAME = "....";

        try {
            BeanWriterBuilder.build(Huge.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        BeanWriter.API_NAME = tmp;
    }

    @Test
    public void testHuge() {
        BeanWriter writer = BeanWriterBuilder.build(Huge.class);

        assert writer.getFields().length == 136;

        Huge huge = new Huge();

        Object[] values = new Object[136];
        for (int i = 0; i < values.length; i++) {
            values[i] = (long) i;
        }

        values[0] = Byte.MAX_VALUE;
        values[1] = 'A';
        values[2] = 1;
        values[3] = "hello";
        values[4] = Short.MAX_VALUE;
        values[5] = Long.MAX_VALUE;

        writer.setValues(huge, values);
    }

    @Data
    public static class Pojo1 {
        private int    id;
        private String _name;
        private String $name;
        private String name;

        public String get$name() {
            return $name;
        }

        public void set$name(String $name) {
            this.$name = $name;
        }
    }

    public static class Pojo2 {
        public int    id;
        public String name;
    }

    @Data
    public static class Huge {
        private byte   b;
        private char   c;
        private int    id;
        private String name;
        private short  s;
        private Long   time;

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
