package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Codec for `java.awt` package, support Point & Dimension & Rectangle & Font & Color & etc.
 *
 * @author sulin
 * @since 2019-07-25 14:28:03
 */
@SuppressWarnings({"MagicConstant"})
public class AwtCodec extends Codec {

    private final String FONT_NAME = "name";
    private final String FONT_STYLE = "style";
    private final String FONT_SIZE = "size";
    private final String POINT_X = "x";
    private final String POINT_Y = "y";
    private final String DIMENSION_WIDTH = "width";
    private final String DIMENSION_HEIGHT = "height";

    /**
     * Convert Map to Font
     */
    @Converter
    public Font toFont(Map map) {
        if (map == null) {
            return null;
        }
        String name = convert(map.get(FONT_NAME), String.class);
        Integer style = convert(map.get(FONT_STYLE), Integer.class);
        Integer size = convert(map.get(FONT_SIZE), Integer.class);
        if (style == null) {
            style = Font.PLAIN;
        }
        if (size == null) {
            size = 0;
        }
        return new Font(name, style, size);
    }

    /**
     * Convert Font to Map
     */
    @Converter
    public Map toMap(Font font) {
        if (font == null)
            return null;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(FONT_NAME, font.getName());
        map.put(FONT_STYLE, font.getStyle());
        map.put(FONT_SIZE, font.getSize());

        return map;
    }

    /**
     * Convert String to Color, support like #001122
     */
    @Converter
    public Color toColor(String s) {
        return s == null ? null : Color.decode(s);
    }

    /**
     * Convert Color to String, like #001122
     */
    @Converter
    public String toString(Color c) {
        return c == null ? null : ("#" + Integer.toString(c.getAlpha(), 16));
    }

    /**
     * Convert Map to Point
     */
    @Converter
    public Point toPoint(Map map) {
        if (map == null)
            return null;
        Integer x = convert(map.get(POINT_X), Integer.class);
        Integer y = convert(map.get(POINT_Y), Integer.class);
        return new Point(x, y);
    }

    /**
     * Convert Map to Dimension
     */
    @Converter
    public Dimension toDemension(Map map) {
        if (map == null)
            return null;
        Integer width = convert(map.get(DIMENSION_WIDTH), Integer.class);
        Integer height = convert(map.get(DIMENSION_HEIGHT), Integer.class);
        return new Dimension(width, height);
    }

    /**
     * Convert Map to Rectangle
     */
    @Converter
    public Rectangle toRectangle(Map map) {
        if (map == null)
            return null;
        Integer x = convert(map.get(POINT_X), Integer.class);
        Integer y = convert(map.get(POINT_Y), Integer.class);
        Integer width = convert(map.get(DIMENSION_WIDTH), Integer.class);
        Integer height = convert(map.get(DIMENSION_HEIGHT), Integer.class);
        return new Rectangle(x, y, width, height);
    }

}
