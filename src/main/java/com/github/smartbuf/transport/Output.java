package com.github.smartbuf.transport;

import com.github.smartbuf.Type;
import com.github.smartbuf.converter.ConverterPipeline;
import com.github.smartbuf.node.Node;
import com.github.smartbuf.node.basic.ObjectNode;
import com.github.smartbuf.reflect.XType;
import com.github.smartbuf.utils.CodecUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

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

    private final boolean  enableStreamMode;
    private final XType<?> nodeXType;

    public final OutputBuffer bodyBuf;
    public final OutputBuffer headBuf;

    public final OutputDataPool dataPool;
    public final OutputMetaPool metaPool;

    private long sequence;

    /**
     * Initialize Output, it is reusable
     *
     * @param enableStreamMode Enable stream-model or not
     */
    public Output(boolean enableStreamMode) {
        this.enableStreamMode = enableStreamMode;
        this.nodeXType = CodecUtils.toXType(Node.class);
        this.bodyBuf = new OutputBuffer(PACKET_LIMIT);
        this.headBuf = new OutputBuffer(PACKET_LIMIT);
        this.dataPool = new OutputDataPool(SYMBOL_LIMIT);
        this.metaPool = new OutputMetaPool(STRUCT_LIMIT);
    }

    /**
     * Output this schema into the specified writer with the specified sequence
     *
     * @param o the object to serialize
     * @return Serialization result
     * @throws IOException if any io exception happens
     */
    public byte[] write(Object o) throws IOException {
        this.writeBuffer(o);
        // build result
        byte[] result = new byte[bodyBuf.offset + headBuf.offset];
        System.arraycopy(headBuf.data, 0, result, 0, headBuf.offset);
        System.arraycopy(bodyBuf.data, 0, result, headBuf.offset, bodyBuf.offset);
        return result;
    }

    public void write(Object o, OutputStream outputStream) throws IOException {
        this.writeBuffer(o);
        outputStream.write(headBuf.data, 0, headBuf.offset);
        outputStream.write(bodyBuf.data, 0, bodyBuf.offset);
    }

    void writeBuffer(Object o) throws IOException {
        this.bodyBuf.reset();
        this.headBuf.reset();
        this.dataPool.reset();
        this.metaPool.reset();
        this.writeObject(o);

        boolean hasData = dataPool.needOutput();
        boolean hasMeta = metaPool.needOutput();
        boolean hasSeq = dataPool.needSequence() || metaPool.needSequence();

        // 1-byte for summary
        byte head = Const.VER;
        if (enableStreamMode) head |= Const.VER_STREAM;
        if (hasData) head |= Const.VER_HAS_DATA;
        if (hasMeta) head |= Const.VER_HAS_META;
        if (hasSeq) head |= Const.VER_HAS_SEQ;
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
    }

    /**
     * Write any object into the buffer, support null.
     */
    void writeObject(Object data) throws IOException {
        byte type;
        if (data == null || data instanceof Boolean) {
            type = Const.TYPE_CONST;
        } else if (data instanceof Byte || data instanceof Short || data instanceof Integer || data instanceof Long) {
            type = Const.TYPE_VARINT;
        } else if (data instanceof Float) {
            type = Const.TYPE_FLOAT;
        } else if (data instanceof Double) {
            type = Const.TYPE_DOUBLE;
        } else if (data instanceof CharSequence || data instanceof Character) {
            type = Const.TYPE_STRING;
        } else if (data instanceof Collection) {
            type = Const.TYPE_ARRAY;
        } else if (data instanceof Enum) {
            type = Const.TYPE_SYMBOL;
            data = ((Enum) data).name();
        } else if (data.getClass().isArray()) {
            if (data instanceof char[]) {
                type = Const.TYPE_STRING;
                data = new String((char[]) data);
            } else if (data instanceof boolean[]) {
                type = Const.TYPE_NARRAY_BOOL;
            } else if (data instanceof byte[]) {
                type = Const.TYPE_NARRAY_BYTE;
            } else if (data instanceof short[]) {
                type = Const.TYPE_NARRAY_SHORT;
            } else if (data instanceof int[]) {
                type = Const.TYPE_NARRAY_INT;
            } else if (data instanceof long[]) {
                type = Const.TYPE_NARRAY_LONG;
            } else if (data instanceof float[]) {
                type = Const.TYPE_NARRAY_FLOAT;
            } else if (data instanceof double[]) {
                type = Const.TYPE_NARRAY_DOUBLE;
            } else {
                type = Const.TYPE_ARRAY;
                data = Arrays.asList((Object[]) data);
            }
        } else {
            Node node;
            if (data instanceof Node) {
                node = (Node) data;
            } else {
                node = CodecUtils.convert(data, Node.class);
            }
            if (node != null) {
                data = node.value();
                switch (node.type()) {
                    case BOOLEAN:
                        type = Const.TYPE_CONST;
                        break;
                    case VARINT:
                        type = Const.TYPE_VARINT;
                        break;
                    case FLOAT:
                        type = Const.TYPE_FLOAT;
                        break;
                    case DOUBLE:
                        type = Const.TYPE_DOUBLE;
                        break;
                    case STRING:
                        type = Const.TYPE_STRING;
                        break;
                    case SYMBOL:
                        type = Const.TYPE_SYMBOL;
                        break;
                    case ARRAY_BOOLEAN:
                        type = Const.TYPE_NARRAY_BOOL;
                        break;
                    case ARRAY_BYTE:
                        type = Const.TYPE_NARRAY_BYTE;
                        break;
                    case ARRAY_SHORT:
                        type = Const.TYPE_NARRAY_SHORT;
                        break;
                    case ARRAY_INT:
                        type = Const.TYPE_NARRAY_INT;
                        break;
                    case ARRAY_LONG:
                        type = Const.TYPE_NARRAY_LONG;
                        break;
                    case ARRAY_FLOAT:
                        type = Const.TYPE_NARRAY_FLOAT;
                        break;
                    case ARRAY_DOUBLE:
                        type = Const.TYPE_NARRAY_DOUBLE;
                        break;
                    case ARRAY:
                        type = Const.TYPE_ARRAY;
                        break;
                    case OBJECT:
                        type = Const.TYPE_OBJECT;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid node: " + node.type());
                }
            } else {
                data = null;
                type = Const.TYPE_CONST;
            }
        }
        this.writeData(type, data);
    }

    /**
     * Do write data with the specified type
     */
    void writeData(byte type, Object data) throws IOException {
        switch (type) {
            case Const.TYPE_CONST:
                if (data == null) {
                    bodyBuf.writeVarUint(Const.CONST_NULL);
                } else {
                    bodyBuf.writeVarUint(((Boolean) data) ? Const.CONST_TRUE : Const.CONST_FALSE);
                }
                break;
            case Const.TYPE_VARINT:
                bodyBuf.writeVarUint((dataPool.registerVarint(((Number) data).longValue()) << 3) | Const.TYPE_VARINT);
                break;
            case Const.TYPE_FLOAT:
                bodyBuf.writeVarUint((dataPool.registerFloat((Float) data) << 3) | Const.TYPE_FLOAT);
                break;
            case Const.TYPE_DOUBLE:
                bodyBuf.writeVarUint((dataPool.registerDouble((Double) data) << 3) | Const.TYPE_DOUBLE);
                break;
            case Const.TYPE_STRING:
                bodyBuf.writeVarUint((dataPool.registerString(data.toString()) << 3) | Const.TYPE_STRING);
                break;
            case Const.TYPE_SYMBOL:
                if (enableStreamMode) {
                    bodyBuf.writeVarUint((dataPool.registerSymbol(data.toString()) << 3) | Const.TYPE_SYMBOL);
                } else {
                    bodyBuf.writeVarUint((dataPool.registerString(data.toString()) << 3) | Const.TYPE_STRING);
                }
                break;
            case Const.TYPE_OBJECT:
                ObjectNode node = (ObjectNode) data;
                if (enableStreamMode && node.isStable()) {
                    bodyBuf.writeVarUint(metaPool.registerCxtStruct(node.keys()) << 3 | Const.TYPE_OBJECT);
                } else {
                    bodyBuf.writeVarUint(metaPool.registerTmpStruct(node.keys()) << 3 | Const.TYPE_OBJECT);
                }
                this.writeObjectNode(node);
                break;
            case Const.TYPE_ARRAY:
                this.writeArray((Collection<?>) data);
                break;
            case Const.TYPE_NARRAY_BOOL:
                boolean[] booleans = (boolean[]) data;
                bodyBuf.writeVarUint(booleans.length << 6 | Const.TYPE_NARRAY_BOOL);
                bodyBuf.writeBooleanArray(booleans);
                break;
            case Const.TYPE_NARRAY_BYTE:
                byte[] bytes = (byte[]) data;
                bodyBuf.writeVarUint(bytes.length << 6 | Const.TYPE_NARRAY_BYTE);
                bodyBuf.writeByteArray(bytes);
                break;
            case Const.TYPE_NARRAY_SHORT:
                short[] shorts = (short[]) data;
                bodyBuf.writeVarUint(shorts.length << 6 | Const.TYPE_NARRAY_SHORT);
                bodyBuf.writeShortArray(shorts);
                break;
            case Const.TYPE_NARRAY_INT:
                int[] ints = (int[]) data;
                bodyBuf.writeVarUint(ints.length << 6 | Const.TYPE_NARRAY_INT);
                bodyBuf.writeIntArray(ints);
                break;
            case Const.TYPE_NARRAY_LONG:
                long[] longs = (long[]) data;
                bodyBuf.writeVarUint(longs.length << 6 | Const.TYPE_NARRAY_LONG);
                bodyBuf.writeLongArray(longs);
                break;
            case Const.TYPE_NARRAY_FLOAT:
                float[] floats = (float[]) data;
                bodyBuf.writeVarUint(floats.length << 6 | Const.TYPE_NARRAY_FLOAT);
                bodyBuf.writeFloatArray(floats);
                break;
            case Const.TYPE_NARRAY_DOUBLE:
                double[] doubles = (double[]) data;
                bodyBuf.writeVarUint(doubles.length << 6 | Const.TYPE_NARRAY_DOUBLE);
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
            bodyBuf.writeVarUint(Const.CONST_ZERO_ARRAY);
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
                itemType = Const.TYPE_SLICE_NULL;
            } else if (pipeline != null && prevCls == itemCls) {
                node = (Node) pipeline.convert(item, nodeXType); // reusing the previous's pipeline
            } else if (prevCls == itemCls) {
                itemType = sliceType; // reusing the previous's type
            } else if (item instanceof Boolean) {
                itemType = Const.TYPE_SLICE_BOOL;
            } else if (item instanceof Byte) {
                itemType = Const.TYPE_SLICE_BYTE;
            } else if (item instanceof Short) {
                itemType = Const.TYPE_SLICE_SHORT;
            } else if (item instanceof Integer) {
                itemType = Const.TYPE_SLICE_INT;
            } else if (item instanceof Long) {
                itemType = Const.TYPE_SLICE_LONG;
            } else if (item instanceof Float) {
                itemType = Const.TYPE_SLICE_FLOAT;
            } else if (item instanceof Double) {
                itemType = Const.TYPE_SLICE_DOUBLE;
            } else if (item instanceof Character || item instanceof CharSequence) {
                itemType = Const.TYPE_SLICE_STRING;
            } else if (item instanceof Enum) {
                itemType = Const.TYPE_SLICE_SYMBOL;
            } else if (item instanceof Collection) {
                itemType = Const.TYPE_SLICE_UNKNOWN;
            } else if (itemCls.isArray()) {
                if (item instanceof char[]) {
                    itemType = Const.TYPE_SLICE_STRING;
                } else {
                    itemType = Const.TYPE_SLICE_UNKNOWN;
                }
            } else {
                pipeline = CodecUtils.getPipeline(itemCls, Node.class);
                node = (Node) pipeline.convert(item, nodeXType);
            }
            if (node != null) {
                item = node.value();
                switch (node.type()) {
                    case BOOLEAN:
                        itemType = Const.TYPE_SLICE_BOOL;
                        break;
                    case DOUBLE:
                        itemType = Const.TYPE_SLICE_DOUBLE;
                        break;
                    case FLOAT:
                        itemType = Const.TYPE_SLICE_FLOAT;
                        break;
                    case VARINT:
                        itemType = Const.TYPE_SLICE_LONG;
                        break;
                    case STRING:
                        itemType = Const.TYPE_SLICE_STRING;
                        break;
                    case SYMBOL:
                        itemType = Const.TYPE_SLICE_SYMBOL;
                        break;
                    case OBJECT:
                        itemKey = ((ObjectNode) node).keys();
                        itemType = Const.TYPE_SLICE_OBJECT;
                        break;
                    default:
                        itemType = Const.TYPE_SLICE_UNKNOWN;
                }
            } else if (itemType == -1) {
                itemType = Const.TYPE_SLICE_NULL;
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
                case Const.TYPE_SLICE_NULL:
                    break;
                case Const.TYPE_SLICE_BOOL:
                    bodyBuf.writeByte(((Boolean) item) ? Const.CONST_TRUE : Const.CONST_FALSE);
                    break;
                case Const.TYPE_SLICE_FLOAT:
                    bodyBuf.writeFloat((Float) item);
                    break;
                case Const.TYPE_SLICE_DOUBLE:
                    bodyBuf.writeDouble((Double) item);
                    break;
                case Const.TYPE_SLICE_BYTE:
                    bodyBuf.writeByte((Byte) item);
                    break;
                case Const.TYPE_SLICE_SHORT:
                    bodyBuf.writeVarInt((Short) item);
                    break;
                case Const.TYPE_SLICE_INT:
                    bodyBuf.writeVarInt((Integer) item);
                    break;
                case Const.TYPE_SLICE_LONG:
                    bodyBuf.writeVarInt((Long) item);
                    break;
                case Const.TYPE_SLICE_STRING:
                    String str;
                    if (item instanceof char[]) {
                        str = new String((char[]) item);
                    } else {
                        str = item.toString();
                    }
                    bodyBuf.writeVarUint(dataPool.registerString(str));
                    break;
                case Const.TYPE_SLICE_SYMBOL:
                    if (item instanceof Enum) {
                        str = ((Enum) item).name();
                    } else {
                        str = item.toString();
                    }
                    bodyBuf.writeVarUint(enableStreamMode ? dataPool.registerSymbol(str) : dataPool.registerString(str));
                    break;
                case Const.TYPE_SLICE_OBJECT:
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
            bodyBuf.writeVarUint(len << 8 | type << 4 | (hasMore ? 0b0000_1000 : 0) | Const.TYPE_ARRAY);
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
                this.writeData(Const.TYPE_CONST, null);
                continue;
            }
            switch (types == null ? Type.UNKNOWN : types[i]) {
                case Z:
                case BOOLEAN:
                    this.writeData(Const.TYPE_CONST, value);
                    break;
                case B:
                case BYTE:
                case S:
                case SHORT:
                case I:
                case INTEGER:
                case J:
                case LONG:
                    this.writeData(Const.TYPE_VARINT, value);
                    break;
                case F:
                case FLOAT:
                    this.writeData(Const.TYPE_FLOAT, value);
                    break;
                case D:
                case DOUBLE:
                    this.writeData(Const.TYPE_DOUBLE, value);
                    break;
                case C:
                case CHAR:
                case STRING:
                    this.writeData(Const.TYPE_STRING, value);
                    break;
                case SYMBOL:
                    this.writeData(Const.TYPE_SYMBOL, ((Enum) value).name());
                    break;
                case ARRAY_BOOL:
                    this.writeData(Const.TYPE_NARRAY_BOOL, value);
                    break;
                case ARRAY_BYTE:
                    this.writeData(Const.TYPE_NARRAY_BYTE, value);
                    break;
                case ARRAY_SHORT:
                    this.writeData(Const.TYPE_NARRAY_SHORT, value);
                    break;
                case ARRAY_INT:
                    this.writeData(Const.TYPE_NARRAY_INT, value);
                    break;
                case ARRAY_LONG:
                    this.writeData(Const.TYPE_NARRAY_LONG, value);
                    break;
                case ARRAY_FLOAT:
                    this.writeData(Const.TYPE_NARRAY_FLOAT, value);
                    break;
                case ARRAY_DOUBLE:
                    this.writeData(Const.TYPE_NARRAY_DOUBLE, value);
                    break;
                case ARRAY_CHAR:
                    this.writeData(Const.TYPE_STRING, new String((char[]) value));
                    break;
                case ARRAY:
                    this.writeData(Const.TYPE_ARRAY, Arrays.asList((Object[]) value));
                    break;
                case COLLECTION:
                    this.writeData(Const.TYPE_ARRAY, value);
                    break;
                default:
                    this.writeObject(value);
                    break;
            }
        }
    }

}
