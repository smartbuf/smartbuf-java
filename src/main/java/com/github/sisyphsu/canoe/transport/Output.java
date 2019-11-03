package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.convertor.CodecFactory;
import com.github.sisyphsu.canoe.convertor.ConverterPipeline;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.node.standard.ObjectNode;
import com.github.sisyphsu.canoe.reflect.XType;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.github.sisyphsu.canoe.transport.Const.*;

/**
 * Output wraps the logic that scans the source data and serializes the output message into highly compressed data
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public final class Output {

    private final boolean      enableStreamMode;
    private final CodecFactory factory   = null;
    private final XType<?>     nodeXType = factory.toXType(Node.class);

    private final Schema schema;

    private final OutputDataPool   dataPool;
    private final OutputStructPool structPool;

    private long         sequence;
    private OutputBuffer bodyBuf = new OutputBuffer(1 << 20);
    private OutputBuffer metaBuf = new OutputBuffer(1 << 24);

    /**
     * Initialize Output, it is reusable
     *
     * @param enableStreamMode Enable stream-model or not
     */
    public Output(boolean enableStreamMode) {
        this.enableStreamMode = enableStreamMode;
        this.schema = new Schema(enableStreamMode);
        this.dataPool = new OutputDataPool(1 << 16);
        this.structPool = new OutputStructPool(1 << 12);
    }

    public void write(Object o) throws IOException {
        this.schema.reset();
        this.dataPool.reset();
        this.structPool.reset();

        this.writeObject(o);
        this.writeMeta();
    }

    void writeMeta() {

    }

    void writeObject(Object obj) throws IOException {
        if (obj == null) {
            bodyBuf.writeVarUint(ID_NULL);
        } else if (obj instanceof Boolean) {
            boolean var = (Boolean) obj;
            bodyBuf.writeVarUint(var ? ID_TRUE : ID_FALSE);
        } else if (obj instanceof Float) {
            bodyBuf.writeVarUint((dataPool.registerFloat((Float) obj) << 3) | DATA_FLOAT);
        } else if (obj instanceof Double) {
            bodyBuf.writeVarUint((dataPool.registerDouble((Double) obj) << 3) | DATA_DOUBLE);
        } else if (obj instanceof Long || obj instanceof Integer || obj instanceof Short || obj instanceof Byte) {
            bodyBuf.writeVarUint((dataPool.registerVarint(((Number) obj).longValue()) << 3) | DATA_VARINT);
        } else if (obj instanceof Character) {
            bodyBuf.writeVarUint((dataPool.registerString(obj.toString()) << 3) | DATA_STRING);
        } else if (obj instanceof CharSequence) {
            bodyBuf.writeVarUint((dataPool.registerString(obj.toString()) << 3) | DATA_STRING);
        } else if (obj instanceof Enum) {
            if (enableStreamMode) {
                bodyBuf.writeVarUint((dataPool.registerSymbol(((Enum) obj).name()) << 3) | DATA_SYMBOL);
            } else {
                bodyBuf.writeVarUint((dataPool.registerString(((Enum) obj).name()) << 3) | DATA_STRING);
            }
        } else if (obj instanceof char[]) {
            bodyBuf.writeVarUint((dataPool.registerString(new String((char[]) obj)) << 3) | DATA_STRING);
        } else if (obj instanceof boolean[]) {
            boolean[] arr = (boolean[]) obj;
            bodyBuf.writeVarUint(arr.length << 6 | NARRAY_BOOL);
            bodyBuf.writeBooleanArray(arr);
        } else if (obj instanceof byte[]) {
            byte[] arr = (byte[]) obj;
            bodyBuf.writeVarUint(arr.length << 6 | NARRAY_BYTE);
            bodyBuf.writeByteArray(arr);
        } else if (obj instanceof short[]) {
            short[] arr = (short[]) obj;
            bodyBuf.writeVarUint(arr.length << 6 | NARRAY_SHORT);
            bodyBuf.writeShortArray(arr);
        } else if (obj instanceof int[]) {
            int[] arr = (int[]) obj;
            bodyBuf.writeVarUint(arr.length << 6 | NARRAY_INT);
            bodyBuf.writeIntArray(arr);
        } else if (obj instanceof long[]) {
            long[] arr = (long[]) obj;
            bodyBuf.writeVarUint(arr.length << 6 | NARRAY_LONG);
            bodyBuf.writeLongArray(arr);
        } else if (obj instanceof float[]) {
            float[] arr = (float[]) obj;
            bodyBuf.writeVarUint(arr.length << 6 | NARRAY_FLOAT);
            bodyBuf.writeFloatArray(arr);
        } else if (obj instanceof double[]) {
            double[] arr = (double[]) obj;
            bodyBuf.writeVarUint(arr.length << 6 | NARRAY_DOUBLE);
            bodyBuf.writeDoubleArray(arr);
        } else if (obj instanceof Object[]) {
            this.writeArray(Arrays.asList((Object[]) obj));
        } else if (obj instanceof List) {
            this.writeArray((List<?>) obj);
        } else if (obj instanceof Collection) {
            this.writeArray((Collection<?>) obj);
        } else if (obj instanceof ObjectNode) {
            ObjectNode node = (ObjectNode) obj;
            if (enableStreamMode && node.isStable()) {
                bodyBuf.writeVarUint(structPool.getCxtStructID(node.keys()) << 3 | DATA_OBJECT);
            } else {
                bodyBuf.writeVarUint(structPool.getTmpStructID(node.keys()) << 3 | DATA_OBJECT);
            }
            for (Object value : node.values()) {
                this.writeObject(value);
            }
        } else {
            Node node = factory.convert(obj, Node.class);
            if (node == null || node instanceof ObjectNode) {
                this.writeObject(node);
            } else {
                this.writeObject(node.value());
            }
        }
    }

    void writeArray(Collection<?> arr) throws IOException {
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
                    pipeline = factory.getPipeline(currCls, Node.class);
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
            if (prevCls != currCls || Arrays.deepEquals(prevKey, currKey)) {
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

    void writeSlice(Class<?> type, Object[] arr, int from, int to, boolean hasMore) throws IOException {
        boolean first = from == 0;
        long len = to - from;
        if (type == null) {
            bodyBuf.writeSliceHead(len, SLICE_NULL, first, hasMore);
        } else if (type == Boolean.class) {
            bodyBuf.writeSliceHead(len, SLICE_BOOL, first, hasMore);
            bodyBuf.writeBooleanSlice(arr, from, to);
        } else if (type == Byte.class) {
            bodyBuf.writeSliceHead(len, SLICE_BYTE, first, hasMore);
            bodyBuf.writeByteSlice(arr, from, to);
        } else if (type == Short.class) {
            bodyBuf.writeSliceHead(len, SLICE_SHORT, first, hasMore);
            bodyBuf.writeShortSlice(arr, from, to);
        } else if (type == Integer.class) {
            bodyBuf.writeSliceHead(len, SLICE_INT, first, hasMore);
            bodyBuf.writeIntSlice(arr, from, to);
        } else if (type == Long.class) {
            bodyBuf.writeSliceHead(len, SLICE_LONG, first, hasMore);
            bodyBuf.writeLongSlice(arr, from, to);
        } else if (type == Float.class) {
            bodyBuf.writeSliceHead(len, SLICE_FLOAT, first, hasMore);
            bodyBuf.writeFloatSlice(arr, from, to);
        } else if (type == Double.class) {
            bodyBuf.writeSliceHead(len, SLICE_DOUBLE, first, hasMore);
            bodyBuf.writeDoubleSlice(arr, from, to);
        } else if (type == Character.class || CharSequence.class.isAssignableFrom(type)) {
            bodyBuf.writeSliceHead(len, SLICE_STRING, first, hasMore);
            for (int i = from; i < to; i++) {
                bodyBuf.writeVarUint(dataPool.registerString(arr[i].toString()));
            }
        } else if (Enum.class.isAssignableFrom(type)) {
            if (enableStreamMode) {
                bodyBuf.writeSliceHead(len, SLICE_SYMBOL, first, hasMore);
                for (int i = from; i < to; i++) {
                    bodyBuf.writeVarUint(dataPool.registerSymbol(((Enum) arr[i]).name()));
                }
            } else {
                bodyBuf.writeSliceHead(len, SLICE_STRING, first, hasMore);
                for (int i = from; i < to; i++) {
                    bodyBuf.writeVarUint(dataPool.registerString(((Enum) arr[i]).name()));
                }
            }
        } else if (type == ObjectNode.class) {
            bodyBuf.writeSliceHead(len, SLICE_OBJECT, first, hasMore);
            ObjectNode node = ((ObjectNode) arr[0]);
            if (enableStreamMode && node.isStable()) {
                bodyBuf.writeVarUint(structPool.getCxtStructID(node.keys()));
            } else {
                bodyBuf.writeVarUint(structPool.getTmpStructID(node.keys()));
            }
            for (int i = from; i < to; i++) {
                node = (ObjectNode) arr[i];
                for (Object value : node.values()) {
                    this.writeObject(value);
                }
            }
        } else {
            bodyBuf.writeSliceHead(len, SLICE_UNKNOWN, first, hasMore);
            for (int i = from; i < to; i++) {
                this.writeObject(arr[i]);
            }
        }
    }


    /**
     * Output this schema into the specified writer with the specified sequence
     */
    public void output() {
        int cxtCount = 0, tmpCount = 0;
        if (dataPool.tmpFloats.size() > 0) tmpCount++;
        if (dataPool.tmpDoubles.size() > 0) tmpCount++;
        if (dataPool.tmpVarints.size() > 0) tmpCount++;
        if (dataPool.tmpStrings.size() > 0) tmpCount++;
        if (dataPool.cxtSymbolAdded.size() > 0) cxtCount++;
        if (dataPool.cxtSymbolExpired.size() > 0) cxtCount++;
        if (structPool.tmpNames.size() > 0) tmpCount++;
        if (structPool.tmpStructs.size() > 0) tmpCount++;
        if (structPool.cxtNameAdded.size() > 0) cxtCount++;
        if (structPool.cxtNameExpired.size() > 0) cxtCount++;
        if (structPool.cxtStructAdded.size() > 0) cxtCount++;
        if (structPool.cxtStructExpired.size() > 0) cxtCount++;

        boolean hasTmpMeta = tmpCount > 0;
        boolean hasCxtMeta = cxtCount > 0;

        // 1-byte for summary
        metaBuf.writeByte((byte) (VER | (enableStreamMode ? VER_STREAM : 0) | (hasTmpMeta ? VER_TMP_META : 0) | (hasCxtMeta ? VER_CXT_META : 0)));
        // 1-byte for context sequence, optional
        if (hasCxtMeta) {
            metaBuf.writeByte((byte) ((++this.sequence) & 0xFF));
        }
        // output temporary metadata
        if (tmpCount > 0) {
            this.writeTmpMeta(tmpCount);
        }
        // output context metadata
        if (cxtCount > 0) {
            this.writeCxtMeta(cxtCount);
        }
    }

    /**
     * Write temporary metadata to the specified writer
     */
    private void writeTmpMeta(int count) {
        int len;
        if ((len = dataPool.tmpFloats.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (TMP_FLOAT << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeFloat(dataPool.tmpFloats.get(i));
            }
        }
        if (count > 0 && (len = dataPool.tmpDoubles.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (TMP_DOUBLE << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeDouble(dataPool.tmpDoubles.get(i));
            }
        }
        if (count > 0 && (len = dataPool.tmpVarints.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (TMP_VARINT << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeVarInt(dataPool.tmpVarints.get(i));
            }
        }
        if (count > 0 && (len = dataPool.tmpStrings.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (TMP_STRING << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeString(dataPool.tmpStrings.get(i));
            }
        }
        if (count > 0 && (len = structPool.tmpNames.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (TMP_NAMES << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeString(structPool.tmpNames.get(i));
            }
        }
        if (count > 0) {
            len = structPool.tmpStructs.size();
            metaBuf.writeVarUint((len << 4) | (TMP_STRUCTS << 1));
            for (int i = 0; i < len; i++) {
                OutputStructPool.Struct struct = structPool.tmpStructs.get(i);
                metaBuf.writeVarUint(struct.nameIds.length);
                for (int nameId : struct.nameIds) {
                    metaBuf.writeVarUint(nameId);
                }
            }
        }
    }

    /**
     * Write context metadata to the specified writer
     */
    private void writeCxtMeta(int count) {
        int len;
        if ((len = structPool.cxtNameAdded.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (CXT_NAME_ADDED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeString(structPool.cxtNameAdded.get(i).name);
            }
        }
        if ((len = structPool.cxtNameExpired.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (CXT_NAME_EXPIRED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeVarUint(structPool.cxtNameExpired.get(i));
            }
        }
        if (count > 0 && (len = structPool.cxtStructAdded.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (CXT_STRUCT_ADDED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                int[] nameIds = structPool.cxtStructAdded.get(i).nameIds;
                metaBuf.writeVarUint(nameIds.length);
                for (int nameId : nameIds) {
                    metaBuf.writeVarUint(nameId);
                }
            }
        }
        if (count > 0 && (len = structPool.cxtStructExpired.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (CXT_STRUCT_EXPIRED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeVarUint(structPool.cxtStructExpired.get(i).index);
            }
        }
        if (count > 0 && (len = dataPool.cxtSymbolAdded.size()) > 0) {
            metaBuf.writeVarUint((len << 4) | (CXT_SYMBOL_ADDED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeString(dataPool.cxtSymbolAdded.get(i).value);
            }
        }
        if (count > 0) {
            len = dataPool.cxtSymbolExpired.size();
            metaBuf.writeVarUint((len << 4) | (CXT_SYMBOL_EXPIRED << 1));
            for (int i = 0; i < len; i++) {
                metaBuf.writeVarUint(dataPool.cxtSymbolExpired.get(i).index);
            }
        }
    }

}
