package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.array.*;
import com.github.sisyphsu.nakedata.node.array.primary.*;
import com.google.common.collect.Lists;

/**
 * Primary array can wrapped as Node directly, for better performance.
 *
 * @author sulin
 * @since 2019-06-05 19:53:57
 */
public class ArrayCodec extends Codec {

    public ArrayNode toArrayNode(boolean[] arr) {
        return ZArrayNode.valueOf(arr);
    }

    public ArrayNode toArrayNode(byte[] arr) {
        return BArrayNode.valueOf(arr);
    }

    public ArrayNode toArrayNode(int[] arr) {
        return IArrayNode.valueOf(arr);
    }

    public ArrayNode toArrayNode(long[] arr) {
        return LArrayNode.valueOf(arr);
    }

    public ArrayNode toArrayNode(short[] arr) {
        return SArrayNode.valueOf(arr);
    }

    public ArrayNode toArrayNode(float[] arr) {
        return FArrayNode.valueOf(arr);
    }

    public ArrayNode toArrayNode(double[] arr) {
        return DArrayNode.valueOf(arr);
    }

    public ArrayNode toArrayNode(Boolean b) {
        return BooleanArrayNode.valueOf(Lists.newArrayList(b));
    }

    public ArrayNode toArrayNode(Byte b) {
        return VarintArrayNode.valueOf(Lists.newArrayList(Long.valueOf(b)));
    }

    public ArrayNode toArrayNode(Short s) {
        return VarintArrayNode.valueOf(Lists.newArrayList(Long.valueOf(s)));
    }

    public ArrayNode toArrayNode(Integer i) {
        return VarintArrayNode.valueOf(Lists.newArrayList(Long.valueOf(i)));
    }

    public ArrayNode toArrayNode(Long l) {
        return VarintArrayNode.valueOf(Lists.newArrayList(l));
    }

    public ArrayNode toArrayNode(Float f) {
        return FloatArrayNode.valueOf(Lists.newArrayList(f));
    }

    public ArrayNode toArrayNode(Double d) {
        return DoubleArrayNode.valueOf(Lists.newArrayList(d));
    }

}
