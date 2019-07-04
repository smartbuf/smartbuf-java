package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.primary.*;
import com.github.sisyphsu.nakedata.node.std.StringNode;

/**
 * Primary array can wrapped as Node directly, for better performance.
 *
 * @author sulin
 * @since 2019-06-05 19:53:57
 */
public class XArrayCodec extends Codec {

    public Node toNode(boolean[] arr) {
        return ZArrayNode.valueOf(arr);
    }

    public Node toNode(byte[] arr) {
        return BArrayNode.valueOf(arr);
    }

    public Node toNode(int[] arr) {
        return IArrayNode.valueOf(arr);
    }

    public Node toNode(long[] arr) {
        return LArrayNode.valueOf(arr);
    }

    public Node toNode(short[] arr) {
        return SArrayNode.valueOf(arr);
    }

    public Node toNode(float[] arr) {
        return FArrayNode.valueOf(arr);
    }

    public Node toNode(double[] arr) {
        return DArrayNode.valueOf(arr);
    }

    public Node toNode(char[] arr) {
        return StringNode.valueOf(String.valueOf(arr));
    }

    public boolean[] toArray(ZArrayNode node) {
        return node.getItems();
    }

    public byte[] toArray(BArrayNode node) {
        return node.getItems();
    }

    public short[] toArray(SArrayNode node) {
        return node.getItems();
    }

    public int[] toArray(IArrayNode node) {
        return node.getItems();
    }

    public long[] toArray(LArrayNode node) {
        return node.getItems();
    }

    public float[] toArray(FArrayNode node) {
        return node.getItems();
    }

    public double[] toArray(DArrayNode node) {
        return node.getItems();
    }

    public char[] toArray(StringNode node) {
        return node.getValue().toCharArray();
    }

}
