package com.github.sisyphsu.nakedata.convertor.codec.awt;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rectangle's codec, include encode and decode functions.
 *
 * @author sulin
 * @since 2019-05-13 18:22:19
 */
public class RectangleCodec extends Codec {

    private static final String R_X = "x";
    private static final String R_Y = "y";
    private static final String R_WIDTH = "width";
    private static final String R_HEIGHT = "height";

    /**
     * Convert Map to Rectangle
     *
     * @param map Map
     * @return Rectangle
     */
    public Rectangle toRectangle(Map map) {
        if (map == null)
            return null;
        Integer x = convert(map.get(R_X), Integer.class);
        Integer y = convert(map.get(R_Y), Integer.class);
        Integer width = convert(map.get(R_WIDTH), Integer.class);
        Integer height = convert(map.get(R_HEIGHT), Integer.class);
        // TODO Null Check
        return new Rectangle(x, y, width, height);
    }

    /**
     * Convert Rectangle to Map
     *
     * @param r Rectangle
     * @return Map
     */
    public Map toMap(Rectangle r) {
        if (r == null)
            return null;
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put(R_X, r.x);
        map.put(R_Y, r.y);
        map.put(R_WIDTH, r.width);
        map.put(R_HEIGHT, r.height);
        return map;
    }

}
