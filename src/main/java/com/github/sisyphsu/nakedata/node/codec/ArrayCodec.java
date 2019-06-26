package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.array.*;
import com.github.sisyphsu.nakedata.node.array.primary.*;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public ArrayNode toArrayNode(Collection coll) {
        if (coll == null) {
            return null;
        }
        if (coll.isEmpty()) {
            return null;
        }
        List<ArrayNode> arrays = new ArrayList<>();
        ArrayNode last = null;
        for (Object item : coll) {
            if (last != null && !last.tryAppend(item)) {
                last = null;
            }
            if (last == null) {
                last = (ArrayNode) convert(item, ArrayNode.class);
                last.tryAppend(item);
                arrays.add(last);
            }
        }
        if (arrays.size() > 1) {
            return MixArrayNode.valueOf(arrays);
        }
        return last;
    }

}
