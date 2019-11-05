package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.convertor.CodecFactory;
import com.github.sisyphsu.canoe.convertor.ConverterPipeline;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.node.basic.ObjectNode;
import com.github.sisyphsu.canoe.reflect.XType;

import java.util.Arrays;
import java.util.Collection;

import static com.github.sisyphsu.canoe.transport.Const.*;

/**
 * Output wraps the logic that scans the source data and serializes the output message into highly compressed data
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public final class Output {

    private final boolean      enableStreamMode;
    private final CodecFactory codecFactory;
    private final XType<?>     nodeXType;

    public final OutputBuffer bodyBuf;
    public final OutputBuffer headBuf;

    public final OutputDataPool dataPool;
    public final OutputMetaPool metaPool;

    private long sequence;

    /**
     * Initialize Output, it is reusable
     *
     * @param codecFactory     Used for data converting
     * @param enableStreamMode Enable stream-model or not
     */
    public Output(CodecFactory codecFactory, boolean enableStreamMode) {
        this.codecFactory = codecFactory;
        this.enableStreamMode = enableStreamMode;
        this.nodeXType = this.codecFactory.toXType(Node.class);
        this.bodyBuf = new OutputBuffer(1 << 30);
        this.headBuf = new OutputBuffer(1 << 24);
        this.dataPool = new OutputDataPool(1 << 16);
        this.metaPool = new OutputMetaPool(1 << 12);
    }

    /**
     * Output this schema into the specified writer with the specified sequence
     */
    public byte[] write(Object o) {
        this.bodyBuf.reset();
        this.headBuf.reset();
        this.dataPool.reset();
        this.metaPool.reset();
        this.writeObject(o);

        boolean hasData = dataPool.needOutput();
        boolean hasMeta = metaPool.needOutput();
        boolean hasSeq = dataPool.needSequence() || metaPool.needSequence();

        // 1-byte for summary
        byte head = VER;
        if (enableStreamMode) head |= VER_STREAM;
        if (hasData) head |= VER_HAS_DATA;
        if (hasMeta) head |= VER_HAS_META;
        if (hasSeq) head |= VER_HAS_SEQ;
        headBuf.writeByte(head);
        // 1-byte for context sequence, if need
        if (hasSeq) {
            headBuf.writeByte((byte) ((++this.sequence) & 0xFF));
        }
        // output sharing meta
        if (hasMeta) {
            metaPool.write(headBuf);
        }
        // output sharing data
        if (hasData) {
            dataPool.write(headBuf);
        }
        // build result
        byte[] result = new byte[bodyBuf.offset + headBuf.offset];
        System.arraycopy(headBuf.data, 0, result, 0, headBuf.offset);
        System.arraycopy(bodyBuf.data, 0, result, headBuf.offset, bodyBuf.offset);
        return result;
    }

    public void writeObject(Object obj) {
        if (obj == null) {
            bodyBuf.writeVarUint(DATA_ID_NULL);
            return;
        }
        if (obj instanceof ObjectNode) {
            ObjectNode node = (ObjectNode) obj;
            if (enableStreamMode && node.isStable()) {
                bodyBuf.writeVarUint(metaPool.registerCxtStruct(node.keys()) << 3 | TYPE_OBJECT);
            } else {
                bodyBuf.writeVarUint(metaPool.registerTmpStruct(node.keys()) << 3 | TYPE_OBJECT);
            }
            for (Object value : node.values()) {
                this.writeObject(value);
            }
        } else if (obj instanceof Boolean) {
            bodyBuf.writeVarUint(((Boolean) obj) ? DATA_ID_TRUE : DATA_ID_FALSE);
        } else if (obj instanceof Byte) {
            bodyBuf.writeVarUint((dataPool.registerVarint((Byte) obj) << 3) | TYPE_VARINT);
        } else if (obj instanceof Short) {
            bodyBuf.writeVarUint((dataPool.registerVarint((Short) obj) << 3) | TYPE_VARINT);
        } else if (obj instanceof Integer) {
            bodyBuf.writeVarUint((dataPool.registerVarint((Integer) obj) << 3) | TYPE_VARINT);
        } else if (obj instanceof Long) {
            bodyBuf.writeVarUint((dataPool.registerVarint((Long) obj) << 3) | TYPE_VARINT);
        } else if (obj instanceof Float) {
            bodyBuf.writeVarUint((dataPool.registerFloat((Float) obj) << 3) | TYPE_FLOAT);
        } else if (obj instanceof Double) {
            bodyBuf.writeVarUint((dataPool.registerDouble((Double) obj) << 3) | TYPE_DOUBLE);
        } else if (obj instanceof CharSequence || obj instanceof Character) {
            bodyBuf.writeVarUint((dataPool.registerString(obj.toString()) << 3) | TYPE_STRING);
        } else if (obj instanceof Collection) {
            this.writeArray((Collection<?>) obj);
        } else if (obj instanceof Enum) {
            if (enableStreamMode) {
                bodyBuf.writeVarUint((dataPool.registerSymbol(((Enum) obj).name()) << 3) | TYPE_SYMBOL);
            } else {
                bodyBuf.writeVarUint((dataPool.registerString(((Enum) obj).name()) << 3) | TYPE_STRING);
            }
        } else {
            Class<?> cls = obj.getClass();
            if (cls.isArray()) {
                if (obj instanceof char[]) {
                    bodyBuf.writeVarUint((dataPool.registerString(new String((char[]) obj)) << 3) | TYPE_STRING);
                } else if (obj instanceof boolean[]) {
                    boolean[] booleans = (boolean[]) obj;
                    bodyBuf.writeVarUint(booleans.length << 6 | TYPE_NARRAY_BOOL);
                    bodyBuf.writeBooleanArray(booleans);
                } else if (obj instanceof byte[]) {
                    byte[] bytes = (byte[]) obj;
                    bodyBuf.writeVarUint(bytes.length << 6 | TYPE_NARRAY_BYTE);
                    bodyBuf.writeByteArray(bytes);
                } else if (obj instanceof short[]) {
                    short[] shorts = (short[]) obj;
                    bodyBuf.writeVarUint(shorts.length << 6 | TYPE_NARRAY_SHORT);
                    bodyBuf.writeShortArray(shorts);
                } else if (obj instanceof int[]) {
                    int[] ints = (int[]) obj;
                    bodyBuf.writeVarUint(ints.length << 6 | TYPE_NARRAY_INT);
                    bodyBuf.writeIntArray(ints);
                } else if (obj instanceof long[]) {
                    long[] longs = (long[]) obj;
                    bodyBuf.writeVarUint(longs.length << 6 | TYPE_NARRAY_LONG);
                    bodyBuf.writeLongArray(longs);
                } else if (obj instanceof float[]) {
                    float[] floats = (float[]) obj;
                    bodyBuf.writeVarUint(floats.length << 6 | TYPE_NARRAY_FLOAT);
                    bodyBuf.writeFloatArray(floats);
                } else if (obj instanceof double[]) {
                    double[] doubles = (double[]) obj;
                    bodyBuf.writeVarUint(doubles.length << 6 | TYPE_NARRAY_DOUBLE);
                    bodyBuf.writeDoubleArray(doubles);
                } else {
                    this.writeArray(Arrays.asList((Object[]) obj));
                }
            } else {
                Node node = codecFactory.convert(obj, Node.class);
                if (node == null || node instanceof ObjectNode) {
                    this.writeObject(node);
                } else {
                    this.writeObject(node.value());
                }
            }
        }
    }

    void writeArray(Collection<?> arr) {
        if (arr.isEmpty()) {
            bodyBuf.writeVarUint(DATA_ID_ZERO_ARRAY);
            return;
        }
        Object[] objects = new Object[arr.size()];
        ConverterPipeline pipeline = null;
        Class<?> prevCls = null;
        Class<?> currCls;
        int off = 0;
        for (Object o : arr) {
            if (o == null || o instanceof Boolean || o instanceof Float || o instanceof Double
                || o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long
                || o instanceof Character || o instanceof CharSequence || o instanceof Enum
                || o instanceof Node) {
                objects[off++] = o;
            } else {
                currCls = o.getClass();
                if (currCls != prevCls || pipeline == null) {
                    prevCls = currCls;
                    pipeline = codecFactory.getPipeline(currCls, Node.class);
                }
                Node node = (Node) pipeline.convert(o, nodeXType);
                if (node == null || node instanceof ObjectNode) {
                    objects[off++] = node;
                } else {
                    objects[off++] = node.value();
                }
            }
        }
        off = 0;
        prevCls = null;
        String[] prevKey = null;
        String[] currKey = null;
        for (int i = 0, len = objects.length; i < len; ) {
            Object o = objects[i];
            if (o == null) {
                currCls = null;
                currKey = null;
            } else {
                currCls = o.getClass();
                if (o instanceof ObjectNode) {
                    currKey = ((ObjectNode) o).keys();
                }
            }
            if (i == 0) {
                prevCls = currCls;
                prevKey = currKey;
            }
            if (prevCls != currCls || !Arrays.deepEquals(prevKey, currKey)) {
                this.writeSlice(prevCls, objects, off, i, true); // write the previous slice
                prevCls = currCls;
                off = i;
            }
            i++;
            if (i == len) {
                this.writeSlice(currCls, objects, off, i, false); // write the last slice
            }
        }
    }

    void writeSlice(Class<?> type, Object[] arr, int from, int to, boolean hasMore) {
        boolean first = from == 0;
        long len = to - from;
        if (type == null) {
            writeSliceHead(len, TYPE_SLICE_NULL, first, hasMore);
        } else if (type == Boolean.class) {
            writeSliceHead(len, TYPE_SLICE_BOOL, first, hasMore);
            bodyBuf.writeBooleanSlice(arr, from, to);
        } else if (type == Byte.class) {
            writeSliceHead(len, TYPE_SLICE_BYTE, first, hasMore);
            bodyBuf.writeByteSlice(arr, from, to);
        } else if (type == Short.class) {
            writeSliceHead(len, TYPE_SLICE_SHORT, first, hasMore);
            bodyBuf.writeShortSlice(arr, from, to);
        } else if (type == Integer.class) {
            writeSliceHead(len, TYPE_SLICE_INT, first, hasMore);
            bodyBuf.writeIntSlice(arr, from, to);
        } else if (type == Long.class) {
            writeSliceHead(len, TYPE_SLICE_LONG, first, hasMore);
            bodyBuf.writeLongSlice(arr, from, to);
        } else if (type == Float.class) {
            writeSliceHead(len, TYPE_SLICE_FLOAT, first, hasMore);
            bodyBuf.writeFloatSlice(arr, from, to);
        } else if (type == Double.class) {
            writeSliceHead(len, TYPE_SLICE_DOUBLE, first, hasMore);
            bodyBuf.writeDoubleSlice(arr, from, to);
        } else if (type == Character.class || CharSequence.class.isAssignableFrom(type)) {
            writeSliceHead(len, TYPE_SLICE_STRING, first, hasMore);
            for (int i = from; i < to; i++) {
                bodyBuf.writeVarUint(dataPool.registerString(arr[i].toString()));
            }
        } else if (Enum.class.isAssignableFrom(type)) {
            if (enableStreamMode) {
                writeSliceHead(len, TYPE_SLICE_SYMBOL, first, hasMore);
                for (int i = from; i < to; i++) {
                    bodyBuf.writeVarUint(dataPool.registerSymbol(((Enum) arr[i]).name()));
                }
            } else {
                writeSliceHead(len, TYPE_SLICE_STRING, first, hasMore);
                for (int i = from; i < to; i++) {
                    bodyBuf.writeVarUint(dataPool.registerString(((Enum) arr[i]).name()));
                }
            }
        } else if (type == ObjectNode.class) {
            writeSliceHead(len, TYPE_SLICE_OBJECT, first, hasMore);
            ObjectNode node = ((ObjectNode) arr[0]);
            if (enableStreamMode && node.isStable()) {
                bodyBuf.writeVarUint(metaPool.registerCxtStruct(node.keys()));
            } else {
                bodyBuf.writeVarUint(metaPool.registerTmpStruct(node.keys()));
            }
            for (int i = from; i < to; i++) {
                node = (ObjectNode) arr[i];
                for (Object value : node.values()) {
                    this.writeObject(value);
                }
            }
        } else {
            writeSliceHead(len, TYPE_SLICE_UNKNOWN, first, hasMore);
            for (int i = from; i < to; i++) {
                this.writeObject(arr[i]);
            }
        }
    }

    void writeSliceHead(long len, byte type, boolean first, boolean hasMore) {
        if (first) {
            bodyBuf.writeVarUint((len << 8) | (type << 4) | (hasMore ? 0b0000_1000 : 0) | TYPE_ARRAY);
        } else {
            bodyBuf.writeVarUint((len << 5) | (type << 1) | (hasMore ? 0b0000_0001 : 0));
        }
    }

}
