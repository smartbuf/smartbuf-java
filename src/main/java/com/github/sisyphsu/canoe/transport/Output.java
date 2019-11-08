package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.converter.CodecFactory;
import com.github.sisyphsu.canoe.converter.ConverterPipeline;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.node.basic.ObjectNode;
import com.github.sisyphsu.canoe.node.basic.SymbolNode;
import com.github.sisyphsu.canoe.reflect.XType;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static com.github.sisyphsu.canoe.transport.Const.*;

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
    void writeObject(Object obj) throws IOException {
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
        } else if (obj instanceof SymbolNode) {
            String name = ((SymbolNode) obj).value().toString();
            if (enableStreamMode) {
                bodyBuf.writeVarUint((dataPool.registerSymbol(name) << 3) | TYPE_SYMBOL);
            } else {
                bodyBuf.writeVarUint((dataPool.registerString(name) << 3) | TYPE_STRING);
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
                if (node == null || node instanceof ObjectNode || node instanceof SymbolNode) {
                    this.writeObject(node);
                } else {
                    this.writeObject(node.value());
                }
            }
        }
    }

    /**
     * Write an array into body, it will scan all items and write those group by different slices
     */
    void writeArray(Collection<?> arr) throws IOException {
        if (arr.isEmpty()) {
            bodyBuf.writeVarUint(DATA_ID_ZERO_ARRAY);
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
                // reusing the previous's pipeline
                node = (Node) pipeline.convert(item, nodeXType);
            } else if (prevCls == itemCls) {
                // reusing the previous's type
                itemType = sliceType;
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
                switch (node.type()) {
                    case BOOLEAN:
                        item = node.value();
                        itemType = TYPE_SLICE_BOOL;
                        break;
                    case DOUBLE:
                        item = node.value();
                        itemType = TYPE_SLICE_DOUBLE;
                        break;
                    case FLOAT:
                        item = node.value();
                        itemType = TYPE_SLICE_FLOAT;
                        break;
                    case VARINT:
                        item = node.value();
                        itemType = TYPE_SLICE_LONG;
                        break;
                    case STRING:
                        item = node.value();
                        itemType = TYPE_SLICE_STRING;
                        break;
                    case SYMBOL:
                        item = node.value();
                        itemType = TYPE_SLICE_SYMBOL;
                        break;
                    case OBJECT:
                        item = node;
                        itemKey = ((ObjectNode) node).keys();
                        itemType = TYPE_SLICE_OBJECT;
                        break;
                    default:
                        item = node.value();
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
                    bodyBuf.writeByte(((Boolean) item) ? DATA_ID_TRUE : DATA_ID_FALSE);
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
                    for (Object value : objectNode.values()) {
                        this.writeObject(value);
                    }
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

}
