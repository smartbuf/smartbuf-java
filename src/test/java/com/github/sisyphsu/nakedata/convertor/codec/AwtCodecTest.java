package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Map;

/**
 * @author sulin
 * @since 2019-08-03 16:43:57
 */
public class AwtCodecTest {

    private AwtCodec codec = new AwtCodec();

    @Before
    public void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testAwt() {
        Rectangle rectangle = new Rectangle(1, 1, 100, 200);
        Map<String, Integer> rectMap = codec.toMap(rectangle);
        assert rectangle.equals(codec.toRectangle(rectMap));

        Dimension dimension = new Dimension(100, 400);
        Map<String, Integer> dimensionMap = codec.toMap(dimension);
        assert dimension.equals(codec.toDimension(dimensionMap));

        Point point = new Point(1, 999);
        Map<String, Integer> pointMap = codec.toMap(point);
        assert point.equals(codec.toPoint(pointMap));

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        Map<String, Object> fontMap = codec.toMap(font);
        assert font.equals(codec.toFont(fontMap));

        String rgb = "#00FF88";
        Color rgbColor = codec.toColor(rgb);
        assert (rgb + "FF").equalsIgnoreCase(codec.toString(rgbColor));

        String rgba = "#00112233";
        Color rgbaColor = codec.toColor(rgba);
        assert rgba.equalsIgnoreCase(codec.toString(rgbaColor));
    }

}