package com.github.sisyphsu.canoe.converter.codec;

import com.github.sisyphsu.canoe.converter.Codec;
import com.github.sisyphsu.canoe.converter.Converter;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Codec for `java.awt` package, support Point & Dimension & Rectangle & Font & Color & etc.
 *
 * @author sulin
 * @since 2019-07-25 14:28:03
 */
@SuppressWarnings({"MagicConstant"})
public final class AwtCodec extends Codec {

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
        String name = convert(map.get(FONT_NAME), String.class);
        Integer style = convert(map.get(FONT_STYLE), Integer.class);
        Integer size = convert(map.get(FONT_SIZE), Integer.class);
        if (style == null) {
            style = Font.PLAIN;
        }
        if (size == null) {
            size = 12;
        }
        return new Font(name, style, size);
    }

    /**
     * Convert Font to Map
     */
    @Converter
    public Map toMap(Font font) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(FONT_NAME, font.getName());
        map.put(FONT_STYLE, font.getStyle());
        map.put(FONT_SIZE, font.getSize());

        return map;
    }

    /**
     * Convert Color to String, like #00112233
     */
    @Converter
    public String toString(Color c) {
        return String.format("#%02x%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    /**
     * Convert String to Color, support like #001122 or #00112233
     */
    @Converter
    public Color toColor(String s) {
        int value = Integer.decode(s);
        if (s.length() == 9 && s.charAt(0) == '#') {
            return new Color(value >> 24 & 0xFF, value >> 16 & 0xFF, value >> 8 & 0xFF, value & 0xFF);
        }
        return new Color((value >> 16) & 0xFF, (value >> 8) & 0xFF, value & 0xFF);
    }

    /**
     * Convert Point to Map
     */
    @Converter
    public Map toMap(Point point) {
        Map<String, Integer> map = new HashMap<>();
        map.put(POINT_X, (int) point.getX());
        map.put(POINT_Y, (int) point.getY());
        return map;
    }

    /**
     * Convert Map to Point
     */
    @Converter
    public Point toPoint(Map map) {
        Integer x = convert(map.get(POINT_X), Integer.class);
        Integer y = convert(map.get(POINT_Y), Integer.class);
        return new Point(x, y);
    }

    /**
     * Convert Dimension to Map
     */
    @Converter
    public Map toMap(Dimension dimension) {
        Map<String, Integer> map = new HashMap<>();
        map.put(DIMENSION_WIDTH, (int) dimension.getWidth());
        map.put(DIMENSION_HEIGHT, (int) dimension.getHeight());
        return map;
    }

    /**
     * Convert Map to Dimension
     */
    @Converter
    public Dimension toDimension(Map map) {
        Integer width = convert(map.get(DIMENSION_WIDTH), Integer.class);
        Integer height = convert(map.get(DIMENSION_HEIGHT), Integer.class);
        return new Dimension(width, height);
    }

    /**
     * Convert Rectangle to Map
     */
    @Converter
    public Map toMap(Rectangle rect) {
        Map<String, Integer> map = new HashMap<>();
        map.put(POINT_X, (int) rect.getX());
        map.put(POINT_Y, (int) rect.getY());
        map.put(DIMENSION_WIDTH, (int) rect.getWidth());
        map.put(DIMENSION_HEIGHT, (int) rect.getHeight());
        return map;
    }

    /**
     * Convert Map to Rectangle
     */
    @Converter
    public Rectangle toRectangle(Map map) {
        Integer x = convert(map.get(POINT_X), Integer.class);
        Integer y = convert(map.get(POINT_Y), Integer.class);
        Integer width = convert(map.get(DIMENSION_WIDTH), Integer.class);
        Integer height = convert(map.get(DIMENSION_HEIGHT), Integer.class);
        return new Rectangle(x, y, width, height);
    }

}
