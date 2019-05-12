package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;
import com.github.sisyphsu.nakedata.node.std.BigIntegerNode;

import java.math.BigInteger;
import java.time.Duration;

/**
 * {@link java.time.Duration}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:34
 */
public class DurationCodec extends Codec<Duration> {

    private static final BigInteger B = BigInteger.valueOf(1000000000);

    public DurationCodec(NodeMapper mapper) {
        super(Duration.class, mapper);
    }

    @Override
    public Node encode(Duration obj) {
        if (obj == null) {
            return BigIntegerNode.NULL;
        }
        BigInteger seconds = BigInteger.valueOf(obj.getSeconds());
        BigInteger nano = BigInteger.valueOf(obj.getNano());
        return BigIntegerNode.valueOf(seconds.multiply(B).add(nano));
    }

    @Override
    public Duration decode(Node node) {
        if (node.isNull()) {
            return null;
        }
        return null;
    }

}
