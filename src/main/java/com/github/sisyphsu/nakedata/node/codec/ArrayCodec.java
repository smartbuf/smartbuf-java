package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.*;
import com.github.sisyphsu.nakedata.node.std.StringNode;

/**
 * Primary array can wrapped as Node directly, for better performance.
 *
 * @author sulin
 * @since 2019-06-05 19:53:57
 */
public class ArrayCodec extends Codec {

    public Node toNode(boolean[] arr) {
        return BooleanArrayNode.valueOf(arr);
    }

    public Node toNode(byte[] arr) {
        return ByteArrayNode.valueOf(arr);
    }

    public Node toNode(int[] arr) {
        return IntArrayNode.valueOf(arr);
    }

    public Node toNode(long[] arr) {
        return LongArrayNode.valueOf(arr);
    }

    public Node toNode(short[] arr) {
        return ShortArrayNode.valueOf(arr);
    }

    public Node toNode(float[] arr) {
        return FloatArrayNode.valueOf(arr);
    }

    public Node toNode(double[] arr) {
        return DoubleArrayNode.valueOf(arr);
    }

    public Node toNode(char[] arr) {
        return StringNode.valueOf(String.valueOf(arr));
    }

}
