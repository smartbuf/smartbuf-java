package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.exception.InvalidReadException;
import com.github.sisyphsu.canoe.exception.InvalidVersionException;
import com.github.sisyphsu.canoe.exception.MismatchModeException;
import com.github.sisyphsu.canoe.exception.UnexpectedSequenceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.sisyphsu.canoe.transport.Const.*;

/**
 * Input accepts and maintains context info that generated by {@link Output},
 * and use it to parse input object one by one.
 *
 * @author sulin
 * @since 2019-10-10 21:43:41
 */
public final class Input {

    private long sequence;

    private final boolean     enableStreamMode;
    private final InputBuffer buffer = new InputBuffer();

    private final InputDataPool dataPool = new InputDataPool();
    private final InputMetaPool metaPool = new InputMetaPool();

    /**
     * Initialize input
     *
     * @param enableStreamMode If enable stream-mode, if not, enable packet-mode
     */
    public Input(boolean enableStreamMode) {
        this.enableStreamMode = enableStreamMode;
    }

    /**
     * Read the next Object from the underlying buffer.
     *
     * @return The next object
     * @throws IOException If any io-error happens
     */
    public Object read(byte[] data) throws IOException {
        buffer.reset(data);

        byte head = buffer.readByte();
        boolean stream = (head & VER_STREAM) != 0;
        boolean hasMeta = (head & VER_HAS_DATA) != 0;
        boolean hasData = (head & VER_HAS_META) != 0;
        boolean hasSeq = (head & VER_HAS_SEQ) != 0;
        // valid schema
        if ((head & 0b1111_0000) != VER) {
            throw new InvalidVersionException(head & 0b1111_0000);
        }
        if (stream != this.enableStreamMode) {
            throw new MismatchModeException(stream);
        }
        // only stream-mode needs sequence
        if (hasSeq) {
            long nextSeq = this.sequence + 1;
            long seq = buffer.readByte();
            if ((seq & 0xFF) != (nextSeq & 0xFF)) {
                throw new UnexpectedSequenceException(seq & 0xFF, (int) (nextSeq & 0xFF));
            }
            this.sequence = nextSeq;
        }
        // read temporary metadata
        if (hasMeta) {
            metaPool.read(buffer);
        }
        // read context metadata
        if (hasData) {
            dataPool.read(buffer);
        }
        // load data
        return readObject();
    }

    /**
     * Read the next node, it could be normal data, array, or struct.
     */
    Object readObject() throws IOException {
        long head = buffer.readVarUint();
        switch ((int) head) {
            case DATA_ID_NULL:
                return null;
            case DATA_ID_TRUE:
                return true;
            case DATA_ID_FALSE:
                return false;
            case DATA_ID_ZERO_ARRAY:
                return new Object[0];
        }
        byte flag = (byte) (head & 0b0000_0011);
        switch (flag) {
            case TYPE_VARINT:
                return dataPool.getVarint((int) (head >>> 3));
            case TYPE_FLOAT:
                return dataPool.getFloat((int) (head >>> 3));
            case TYPE_DOUBLE:
                return dataPool.getDouble((int) (head >>> 3));
            case TYPE_STRING:
                return dataPool.getString((int) (head >>> 3));
            case TYPE_SYMBOL:
                return dataPool.getSymbol((int) (head >>> 3));
            case TYPE_NARRAY:
                return readNArray(head);
            case TYPE_ARRAY:
                return this.readArray(head >>> 3);
            case TYPE_OBJECT:
                String[] fields = metaPool.findStructByID((int) (head >>> 3));
                Map<String, Object> map = new HashMap<>();
                for (String field : fields) {
                    map.put(field, readObject());
                }
                return map;
            default:
                throw new InvalidReadException("run into invalid data flag: " + flag);
        }
    }

    /**
     * Read an native array, like byte[] int[]
     */
    Object readNArray(long head) throws IOException {
        byte type = (byte) (head & 0b0011_1111);
        int size = (int) (head >>> 6);
        switch (type) {
            case TYPE_NARRAY_BOOL:
                return buffer.readBooleanArray(size);
            case TYPE_NARRAY_BYTE:
                return buffer.readByteArray(size);
            case TYPE_NARRAY_SHORT:
                return buffer.readShortArray(size);
            case TYPE_NARRAY_INT:
                return buffer.readIntArray(size);
            case TYPE_NARRAY_LONG:
                return buffer.readLongArray(size);
            case TYPE_NARRAY_FLOAT:
                return buffer.readFloatArray(size);
            case TYPE_NARRAY_DOUBLE:
                return buffer.readDoubleArray(size);
            default:
                throw new IllegalArgumentException("unknown narray type");
        }
    }

    /**
     * Read an array by the specified head info
     */
    Object readArray(long head) throws IOException {
        List<Object[]> slices = new ArrayList<>(2);
        int totalSize = 0;
        while (true) {
            byte type = (byte) ((head >>> 1) & 0x0F);
            int size = (int) (head >>> 5);
            totalSize += size;
            Object[] slice;
            switch (type) {
                case TYPE_SLICE_NULL:
                    slice = new Object[size];
                    break;
                case TYPE_SLICE_BOOL:
                    slice = buffer.readBooleanSlice(size);
                    break;
                case TYPE_SLICE_BYTE:
                    slice = buffer.readByteSlice(size);
                    break;
                case TYPE_SLICE_SHORT:
                    slice = buffer.readShortSlice(size);
                    break;
                case TYPE_SLICE_INT:
                    slice = buffer.readIntSlice(size);
                    break;
                case TYPE_SLICE_LONG:
                    slice = buffer.readLongSlice(size);
                    break;
                case TYPE_SLICE_FLOAT:
                    slice = buffer.readFloatSlice(size);
                    break;
                case TYPE_SLICE_DOUBLE:
                    slice = buffer.readDoubleSlice(size);
                    break;
                case TYPE_SLICE_SYMBOL:
                    slice = new String[size];
                    for (int i = 0; i < size; i++) {
                        int dataId = (int) buffer.readVarUint();
                        slice[i] = enableStreamMode ? dataPool.getSymbol(dataId) : dataPool.getString(dataId);
                    }
                    break;
                case TYPE_SLICE_STRING:
                    slice = new Object[size];
                    for (int i = 0; i < size; i++) {
                        slice[i] = dataPool.getString((int) buffer.readVarUint());
                    }
                    break;
                case TYPE_SLICE_OBJECT:
                    slice = new Object[size];
                    int structId = (int) buffer.readVarUint();
                    String[] fieldNames = metaPool.findStructByID(structId);
                    for (int i = 0; i < size; i++) {
                        Map<String, Object> obj = new HashMap<>();
                        for (String field : fieldNames) {
                            obj.put(field, readObject());
                        }
                        slice[i] = obj;
                    }
                    break;
                case TYPE_SLICE_UNKNOWN:
                    slice = new Object[size];
                    for (int i = 0; i < size; i++) {
                        slice[i] = readObject();
                    }
                    break;
                default:
                    throw new InvalidReadException("run into invalid slice type: " + type);
            }
            slices.add(slice);
            if ((head & 1) == 0) {
                break;
            }
            head = buffer.readVarUint();
        }
        if (slices.size() == 1) {
            return slices.get(0);
        }
        Object[] result = new Object[totalSize];
        int off = 0;
        for (Object[] slice : slices) {
            for (Object o : slice) {
                result[off++] = o;
            }
        }
        return result;
    }

}
