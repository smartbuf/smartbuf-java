package com.github.sisyphsu.nakedata.convertor.codec.awt;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Font's Codec, includes encode and decode function.
 *
 * @author sulin
 * @since 2019-05-13 18:22:01
 */
public class FontCodec extends Codec {

    private final String FONT_NAME = "name";
    private final String FONT_STYLE = "style";
    private final String FONT_SIZE = "size";

    /**
     * Convert Map to Font
     *
     * @param map Map
     * @return Font
     */
    @SuppressWarnings({"MagicConstant"})
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
     *
     * @param font Font
     * @return Map
     */
    public Map toMap(Font font) {
        if (font == null)
            return null;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(FONT_NAME, font.getName());
        map.put(FONT_STYLE, font.getStyle());
        map.put(FONT_SIZE, font.getSize());

        return map;
    }

}
