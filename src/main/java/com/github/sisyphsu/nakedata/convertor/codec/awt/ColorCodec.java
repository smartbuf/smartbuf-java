package com.github.sisyphsu.nakedata.convertor.codec.awt;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.awt.*;

/**
 * Color's codec, include encode and decode functions.
 *
 * @author sulin
 * @since 2019-05-13 18:21:55
 */
public class ColorCodec extends Codec {

    /**
     * Convert String to Color, support like #001122
     *
     * @param s String
     * @return Color
     */
    @Converter
    public Color toColor(String s) {
        return Color.decode(s);
    }

    /**
     * Convert Color to String, like #001122
     *
     * @param c Color
     * @return String
     */
    @Converter
    public String toString(Color c) {
        return "#" + Integer.toString(c.getAlpha(), 16);
    }

}
