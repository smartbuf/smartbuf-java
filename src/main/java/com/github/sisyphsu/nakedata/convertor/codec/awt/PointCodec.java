package com.github.sisyphsu.nakedata.convertor.codec.awt;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Point's codec, includes encode and decode functions.
 *
 * @author sulin
 * @since 2019-05-13 18:22:10
 */
public class PointCodec extends Codec {

    private static final String P_X = "x";
    private static final String P_Y = "y";

    /**
     * Convert Map to Point
     *
     * @param map Map
     * @return Point
     */
    public Point toPoint(Map map) {
        if (map == null)
            return null;
        Integer x = convert(map.get(P_X), Integer.class);
        Integer y = convert(map.get(P_Y), Integer.class);
        // TODO Null check
        return new Point(x, y);
    }

    /**
     * Convert Point to Long
     *
     * @param p Point
     * @return Long
     */
    public Map toLong(Point p) {
        if (p == null)
            return null;
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put(P_X, p.x);
        map.put(P_Y, p.y);
        return map;
    }

}
