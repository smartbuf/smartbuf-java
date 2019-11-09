package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.Type;
import com.github.sisyphsu.canoe.converter.CodecFactory;
import com.github.sisyphsu.canoe.converter.ConverterPipeline;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.node.basic.ObjectNode;
import com.github.sisyphsu.canoe.reflect.XType;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static com.github.sisyphsu.canoe.Const.*;

/**
 * Output wraps the logic that scans the source data and serializes the output message into highly compressed data
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public final class Output {

    public static int PACKET_LIMIT = 64 * 1024 * 1024;
    public static int SYMBOL_LIMIT = 1 << 16;
    public static int STRUCT_LIMIT = 1 << 16;

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
        this.bodyBuf = new OutputBuffer(PACKET_LIMIT);
        this.headBuf = new OutputBuffer(PACKET_LIMIT);
        this.dataPool = new OutputDataPool(SYMBOL_LIMIT);
        this.metaPool = new OutputMetaPool(STRUCT_LIMIT);
    }

    /**
     * Output this schema into the specified writer with the specified sequence
     */
    public byte[] write(Object o) throws IOException {
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

    /**
     * Write any object into the buffer, support null.
     */
    void writeObject(Object data) throws IOException {
        byte type;
        if (data == null || data instanceof Boolean) {
            type = TYPE_CONST;
        } else if (data instanceof Byte || data instanceof Short || data instanceof Integer || data instanceof Long) {
            type = TYPE_VARINT;
        } else if (data instanceof Float) {
            type = TYPE_FLOAT;
        } else if (data instanceof Double) {
            type = TYPE_DOUBLE;
        } else if (data instanceof CharSequence || data instanceof Character) {
            type = TYPE_STRING;
        } else if (data instanceof Collection) {
            type = TYPE_ARRAY;
        } else if (data instanceof Enum) {
            type = TYPE_SYMBOL;
            data = ((Enum) data).name();
        } else if (data.getClass().isArray()) {
            if (data instanceof char[]) {
                type = TYPE_STRING;
                data = new String((char[]) data);
            } else if (data instanceof boolean[]) {
                type = TYPE_NARRAY_BOOL;
            } else if (data instanceof byte[]) {
                type = TYPE_NARRAY_BYTE;
            } else if (data instanceof short[]) {
                type = TYPE_NARRAY_SHORT;
            } else if (data instanceof int[]) {
                type = TYPE_NARRAY_INT;
            } else if (data instanceof long[]) {
                type = TYPE_NARRAY_LONG;
            } else if (data instanceof float[]) {
                type = TYPE_NARRAY_FLOAT;
            } else if (data instanceof double[]) {
                type = TYPE_NARRAY_DOUBLE;
            } else {
                type = TYPE_ARRAY;
                data = Arrays.asList((Object[]) data);
            }
        } else {
            Node node;
            if (data instanceof Node) {
                node = (Node) data;
            } else {
                node = codecFactory.convert(data, Node.class);
            }
            if (node != null) {
                data = node.value();
                type = node.type();
            } else {
                data = null;
                type = TYPE_CONST;
            }
        }
        this.writeData(type, data);
    }

    /**
     * Do write data with the specified type
     */
    void writeData(byte type, Object data) throws IOException {
        switch (type) {
            case TYPE_CONST:
                if (data == null) {
                    bodyBuf.writeVarUint(CONST_NULL);
                } else {
                    bodyBuf.writeVarUint(((Boolean) data) ? CONST_TRUE : CONST_FALSE);
                }
                break;
            case TYPE_VARINT:
                bodyBuf.writeVarUint((dataPool.registerVarint(((Number) data).longValue()) << 3) | TYPE_VARINT);
                break;
            case TYPE_FLOAT:
                bodyBuf.writeVarUint((dataPool.registerFloat((Float) data) << 3) | TYPE_FLOAT);
                break;
            case TYPE_DOUBLE:
                bodyBuf.writeVarUint((dataPool.registerDouble((Double) data) << 3) | TYPE_DOUBLE);
                break;
            case TYPE_STRING:
                bodyBuf.writeVarUint((dataPool.registerString(data.toString()) << 3) | TYPE_STRING);
                break;
            case TYPE_SYMBOL:
                if (enableStreamMode) {
                    bodyBuf.writeVarUint((dataPool.registerSymbol(data.toString()) << 3) | TYPE_SYMBOL);
                } else {
                    bodyBuf.writeVarUint((dataPool.registerString(data.toString()) << 3) | TYPE_STRING);
                }
                break;
            case TYPE_OBJECT:
                ObjectNode node = (ObjectNode) data;
                if (enableStreamMode && node.isStable()) {
                    bodyBuf.writeVarUint(metaPool.registerCxtStruct(node.keys()) << 3 | TYPE_OBJECT);
                } else {
                    bodyBuf.writeVarUint(metaPool.registerTmpStruct(node.keys()) << 3 | TYPE_OBJECT);
                }
                this.writeObjectNode(node);
                break;
            case TYPE_ARRAY:
                this.writeArray((Collection<?>) data);
                break;
            case TYPE_NARRAY_BOOL:
                boolean[] booleans = (boolean[]) data;
                bodyBuf.writeVarUint(booleans.length << 6 | TYPE_NARRAY_BOOL);
                bodyBuf.writeBooleanArray(booleans);
                break;
            case TYPE_NARRAY_BYTE:
                byte[] bytes = (byte[]) data;
                bodyBuf.writeVarUint(bytes.length << 6 | TYPE_NARRAY_BYTE);
                bodyBuf.writeByteArray(bytes);
                break;
            case TYPE_NARRAY_SHORT:
                short[] shorts = (short[]) data;
                bodyBuf.writeVarUint(shorts.length << 6 | TYPE_NARRAY_SHORT);
                bodyBuf.writeShortArray(shorts);
                break;
            case TYPE_NARRAY_INT:
                int[] ints = (int[]) data;
                bodyBuf.writeVarUint(ints.length << 6 | TYPE_NARRAY_INT);
                bodyBuf.writeIntArray(ints);
                break;
            case TYPE_NARRAY_LONG:
                long[] longs = (long[]) data;
                bodyBuf.writeVarUint(longs.length << 6 | TYPE_NARRAY_LONG);
                bodyBuf.writeLongArray(longs);
                break;
            case TYPE_NARRAY_FLOAT:
                float[] floats = (float[]) data;
                bodyBuf.writeVarUint(floats.length << 6 | TYPE_NARRAY_FLOAT);
                bodyBuf.writeFloatArray(floats);
                break;
            case TYPE_NARRAY_DOUBLE:
                double[] doubles = (double[]) data;
                bodyBuf.writeVarUint(doubles.length << 6 | TYPE_NARRAY_DOUBLE);
                bodyBuf.writeDoubleArray(doubles);
                break;
            default:
                throw new IllegalArgumentException("invalid type: " + type);
        }
    }

    /**
     * Write an array into body, it will scan all items and write those group by different slices
     */
    void writeArray(Collection<?> arr) throws IOException {
        if (arr.isEmpty()) {
            bodyBuf.writeVarUint(CONST_ZERO_ARRAY);
            return;
        }

        byte sliceType = -1;
        int sliceLen = 0;
        int sliceHeadOffset = 0;
        String[] sliceKey = null;
        boolean isFirstSlice = true;

        // loop write all items
        ConverterPipeline pipeline = null;
        Class<?> prevCls = null;
        for (Iterator it = arr.iterator(); ; ) {
            Object item = it.next();
            Class<?> itemCls = item == null ? null : item.getClass();
            String[] itemKey = null;
            byte itemType = -1;
            // determine the current item's metadata
            Node node = null;
            if (item == null) {
                itemType = TYPE_SLICE_NULL;
            } else if (pipeline != null && prevCls == itemCls) {
                node = (Node) pipeline.convert(item, nodeXType); // reusing the previous's pipeline
            } else if (prevCls == itemCls) {
                itemType = sliceType; // reusing the previous's type
            } else if (item instanceof Boolean) {
                itemType = TYPE_SLICE_BOOL;
            } else if (item instanceof Byte) {
                itemType = TYPE_SLICE_BYTE;
            } else if (item instanceof Short) {
                itemType = TYPE_SLICE_SHORT;
            } else if (item instanceof Integer) {
                itemType = TYPE_SLICE_INT;
            } else if (item instanceof Long) {
                itemType = TYPE_SLICE_LONG;
            } else if (item instanceof Float) {
                itemType = TYPE_SLICE_FLOAT;
            } else if (item instanceof Double) {
                itemType = TYPE_SLICE_DOUBLE;
            } else if (item instanceof Character || item instanceof CharSequence) {
                itemType = TYPE_SLICE_STRING;
            } else if (item instanceof Enum) {
                itemType = TYPE_SLICE_SYMBOL;
            } else if (item instanceof Collection) {
                itemType = TYPE_SLICE_UNKNOWN;
            } else if (itemCls.isArray()) {
                if (item instanceof char[]) {
                    itemType = TYPE_SLICE_STRING;
                } else {
                    itemType = TYPE_SLICE_UNKNOWN;
                }
            } else {
                pipeline = codecFactory.getPipeline(itemCls, Node.class);
                node = (Node) pipeline.convert(item, nodeXType);
            }
            if (node != null) {
                item = node.value();
                switch (node.type()) {
                    case TYPE_CONST:
                        itemType = TYPE_SLICE_BOOL;
                        break;
                    case TYPE_DOUBLE:
                        itemType = TYPE_SLICE_DOUBLE;
                        break;
                    case TYPE_FLOAT:
                        itemType = TYPE_SLICE_FLOAT;
                        break;
                    case TYPE_VARINT:
                        itemType = TYPE_SLICE_LONG;
                        break;
                    case TYPE_STRING:
                        itemType = TYPE_SLICE_STRING;
                        break;
                    case TYPE_SYMBOL:
                        itemType = TYPE_SLICE_SYMBOL;
                        break;
                    case TYPE_OBJECT:
                        itemKey = ((ObjectNode) node).keys();
                        itemType = TYPE_SLICE_OBJECT;
                        break;
                    default:
                        itemType = TYPE_SLICE_UNKNOWN;
                }
            } else if (itemType == -1) {
                itemType = TYPE_SLICE_NULL;
            }
            prevCls = itemCls;

            // terminate the previous slice if it's broken
            boolean typeBroken = sliceType >= 0 && (sliceType != itemType || !Arrays.equals(sliceKey, itemKey));
            boolean hitLimit = (sliceLen + 1) >= (isFirstSlice ? (1 << 6) : (1 << 11));
            if (typeBroken || hitLimit) {
                this.writeSliceMetadata(sliceHeadOffset, isFirstSlice, sliceLen, sliceType, true);
                isFirstSlice = false;
            }

            // prepare for next slice
            if (sliceType == -1 || typeBroken || hitLimit) {
                sliceType = itemType;
                sliceKey = itemKey;
                sliceLen = 0;
                sliceHeadOffset = bodyBuf.offset;
                bodyBuf.offset += 2; // skip 2-byte for storing slice metadata
            }

            // output current item
            switch (itemType) {
                case TYPE_SLICE_NULL:
                    break;
                case TYPE_SLICE_BOOL:
                    bodyBuf.writeByte(((Boolean) item) ? CONST_TRUE : CONST_FALSE);
                    break;
                case TYPE_SLICE_FLOAT:
                    bodyBuf.writeFloat((Float) item);
                    break;
                case TYPE_SLICE_DOUBLE:
                    bodyBuf.writeDouble((Double) item);
                    break;
                case TYPE_SLICE_BYTE:
                    bodyBuf.writeByte((Byte) item);
                    break;
                case TYPE_SLICE_SHORT:
                    bodyBuf.writeVarInt((Short) item);
                    break;
                case TYPE_SLICE_INT:
                    bodyBuf.writeVarInt((Integer) item);
                    break;
                case TYPE_SLICE_LONG:
                    bodyBuf.writeVarInt((Long) item);
                    break;
                case TYPE_SLICE_STRING:
                    String str;
                    if (item instanceof char[]) {
                        str = new String((char[]) item);
                    } else {
                        str = item.toString();
                    }
                    bodyBuf.writeVarUint(dataPool.registerString(str));
                    break;
                case TYPE_SLICE_SYMBOL:
                    if (item instanceof Enum) {
                        str = ((Enum) item).name();
                    } else {
                        str = item.toString();
                    }
                    bodyBuf.writeVarUint(enableStreamMode ? dataPool.registerSymbol(str) : dataPool.registerString(str));
                    break;
                case TYPE_SLICE_OBJECT:
                    ObjectNode objectNode = (ObjectNode) item;
                    if (sliceLen == 0) {
                        if (enableStreamMode && objectNode.isStable()) {
                            bodyBuf.writeVarUint(metaPool.registerCxtStruct(objectNode.keys()));
                        } else {
                            bodyBuf.writeVarUint(metaPool.registerTmpStruct(objectNode.keys()));
                        }
                    }
                    this.writeObjectNode(objectNode);
                    break;
                default:
                    this.writeObject(item);
            }
            sliceLen++;
            // output the last slice's metadata if need
            if (!it.hasNext()) {
                this.writeSliceMetadata(sliceHeadOffset, isFirstSlice, sliceLen, sliceType, false);
                break;
            }
        }
    }

    /**
     * Write a 2-byte metadata of Fixed-Sliceinto the specified position
     */
    private void writeSliceMetadata(int headOffset, boolean isFirst, int len, byte type, boolean hasMore) throws IOException {
        int tmp = bodyBuf.offset;
        bodyBuf.offset = headOffset;
        if (isFirst) {
            // need compatible with writeObject, must write 2-byte varint
            bodyBuf.writeVarUint(len << 8 | type << 4 | (hasMore ? 0b0000_1000 : 0) | TYPE_ARRAY);
        } else {
            bodyBuf.writeShort((short) (len << 5 | type << 1 | (hasMore ? 0b0000_0001 : 0)));
        }
        bodyBuf.offset = tmp;
    }

    /**
     * Write the specified ObjectNode into output buffer
     */
    private void writeObjectNode(ObjectNode node) throws IOException {
        Object[] values = node.values();
        Type[] types = node.types();
        for (int i = 0, len = values.length; i < len; i++) {
            Object value = values[i];
            if (value == null) {
                this.writeData(TYPE_CONST, null);
                continue;
            }
            switch (types == null ? Type.UNKNOWN : types[i]) {
                case Z:
                case BOOL:
                    this.writeData(TYPE_CONST, value);
                    break;
                case B:
                case BYTE:
                case S:
                case SHORT:
                case I:
                case INTEGER:
                case J:
                case LONG:
                    this.writeData(TYPE_VARINT, value);
                    break;
                case F:
                case FLOAT:
                    this.writeData(TYPE_FLOAT, value);
                    break;
                case D:
                case DOUBLE:
                    this.writeData(TYPE_DOUBLE, value);
                    break;
                case C:
                case CHAR:
                case STRING:
                    this.writeData(TYPE_STRING, value);
                    break;
                case ENUM:
                    this.writeData(TYPE_SYMBOL, ((Enum) value).name());
                    break;
                case ARRAY_BOOL:
                    this.writeData(TYPE_NARRAY_BOOL, value);
                    break;
                case ARRAY_BYTE:
                    this.writeData(TYPE_NARRAY_BYTE, value);
                    break;
                case ARRAY_SHORT:
                    this.writeData(TYPE_NARRAY_SHORT, value);
                    break;
                case ARRAY_INT:
                    this.writeData(TYPE_NARRAY_INT, value);
                    break;
                case ARRAY_LONG:
                    this.writeData(TYPE_NARRAY_LONG, value);
                    break;
                case ARRAY_FLOAT:
                    this.writeData(TYPE_NARRAY_FLOAT, value);
                    break;
                case ARRAY_DOUBLE:
                    this.writeData(TYPE_NARRAY_DOUBLE, value);
                    break;
                case ARRAY_CHAR:
                    this.writeData(TYPE_STRING, new String((char[]) value));
                    break;
                case ARRAY:
                    this.writeData(TYPE_ARRAY, Arrays.asList((Object[]) value));
                    break;
                case COLLECTION:
                    this.writeData(TYPE_ARRAY, value);
                    break;
                case UNKNOWN:
                    this.writeObject(value);
                    break;
            }
        }
    }

}
