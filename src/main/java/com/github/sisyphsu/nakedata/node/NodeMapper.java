package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.reflect.XTypeUtils;

/**
 * @author sulin
 * @since 2019-09-21 13:50:31
 */
public final class NodeMapper {

    private static final CodecFactory CODEC_FACTORY = new CodecFactory();

    static {
        CODEC_FACTORY.installCodec(ArrayNodeCodec.class);
        CODEC_FACTORY.installCodec(BasicNodeCodec.class);
        CODEC_FACTORY.installCodec(BeanNodeCodec.class);
    }

    public static Node convertNodeTree(Object obj) {
        return (Node) CODEC_FACTORY.doConvert(obj, XTypeUtils.toXType(Node.class));
    }

}
