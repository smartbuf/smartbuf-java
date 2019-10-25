package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.IOReader;
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
 * @author sulin
 * @since 2019-10-10 21:43:41
 */
public final class Input {

    private long sequence;

    private final boolean      stream;
    private final Schema       schema;
    private final InputReader  reader;
    private final InputContext context;

    public Input(IOReader reader, boolean enableStreamMode) {
        this.stream = enableStreamMode;
        this.schema = new Schema(enableStreamMode);
        this.reader = new InputReader(reader);
        this.context = new InputContext(schema);
    }

    public Object read() throws IOException {
        schema.reset();
        schema.read(reader);
        // valid schema
        if ((schema.head & 0b1111_0000) != VER) {
            throw new InvalidVersionException(schema.head & 0b1111_0000);
        }
        if (schema.stream != stream) {
            throw new MismatchModeException(stream);
        }
        if (schema.hasCxtMeta) {
            if ((schema.sequence & 0xFF) != ((sequence + 1) & 0xFF)) {
                throw new UnexpectedSequenceException(schema.sequence & 0xFF, (int) ((sequence + 1) & 0xFF));
            }
            this.sequence++;
        }
        // sync context
        this.context.sync();

        // load data
        return readNode();
    }

    Object readNode() throws IOException {
        long nodeID = reader.readVarUint();
        byte flag = (byte) (nodeID & 0b0000_0011);
        switch (flag) {
            case FLAG_DATA:
                return context.findDataByID((int) (nodeID >>> 2));
            case FLAG_ARRAY:
                return this.readArray(nodeID >>> 2);
            case FLAG_STRUCT:
                String[] fields = context.findStructByID((int) (nodeID >>> 2));
                Map<String, Object> tmp = new HashMap<>();
                for (String field : fields) {
                    tmp.put(field, readNode());
                }
                return tmp;
            default:
                throw new InvalidReadException("run into invalid data flag: " + flag);
        }
    }

    /**
     * Read an array
     */
    Object readArray(long head) throws IOException {
        if (head == ID_ZERO_ARRAY) {
            return new Object[0];
        }
        if ((head & 1) == 0) {
            return readPureArray(head);
        }
        List<Object[]> slices = new ArrayList<>();
        int totalSize = 0;
        while (true) {
            byte type = (byte) ((head >>> 1) & 0x0F);
            int size = (int) (head >>> 5);
            totalSize += size;
            Object[] slice;
            switch (type) {
                case SLICE_BOOL:
                    slice = reader.readBooleanSlice(size);
                    break;
                case SLICE_BYTE:
                    slice = reader.readByteSlice(size);
                    break;
                case SLICE_SHORT:
                    slice = reader.readShortSlice(size);
                    break;
                case SLICE_INT:
                    slice = reader.readIntSlice(size);
                    break;
                case SLICE_LONG:
                    slice = reader.readLongSlice(size);
                    break;
                case SLICE_FLOAT:
                    slice = reader.readFloatSlice(size);
                    break;
                case SLICE_DOUBLE:
                    slice = reader.readDoubleSlice(size);
                    break;
                case SLICE_NULL:
                    slice = new Object[size];
                    break;
                case SLICE_SYMBOL:
                    slice = new String[size];
                    for (int i = 0; i < size; i++) {
                        int dataId = (int) reader.readVarUint();
                        slice[i] = stream ? context.findSymbolByID(dataId) : context.findStringByID(dataId);
                    }
                    break;
                case SLICE_STRING:
                    slice = new String[size];
                    for (int i = 0; i < size; i++) {
                        slice[i] = context.findStringByID((int) reader.readVarUint());
                    }
                    break;
                case SLICE_ARRAY:
                    slice = new Object[size];
                    for (int i = 0; i < size; i++) {
                        slice[i] = readArray(reader.readVarUint());
                    }
                    break;
                case SLICE_OBJECT:
                    slice = new Object[size];
                    int structId = (int) reader.readVarUint();
                    String[] fieldNames = context.findStructByID(structId);
                    for (int i = 0; i < size; i++) {
                        Map<String, Object> obj = new HashMap<>();
                        for (String field : fieldNames) {
                            obj.put(field, readNode());
                        }
                        slice[i] = obj;
                    }
                    break;
                default:
                    throw new InvalidReadException("run into invalid slice type: " + type);
            }
            slices.add(slice);
            if ((head & 1) == 0) {
                break;
            }
            head = reader.readVarUint();
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

    /**
     * Read a pure array, which means all items has same type
     */
    Object readPureArray(long head) throws IOException {
        byte type = (byte) ((head >>> 1) & 0x0F);
        int size = (int) (head >>> 5);
        switch (type) {
            case SLICE_NULL:
                return new Object[size];
            case SLICE_BOOL:
                return reader.readBooleanArray(size);
            case SLICE_BYTE:
                return reader.readByteArray(size);
            case SLICE_SHORT:
                return reader.readShortArray(size);
            case SLICE_INT:
                return reader.readIntArray(size);
            case SLICE_LONG:
                return reader.readLongArray(size);
            case SLICE_FLOAT:
                return reader.readFloatArray(size);
            case SLICE_DOUBLE:
                return reader.readDoubleArray(size);
            case SLICE_STRING:
                String[] strings = new String[size];
                for (int i = 0; i < size; i++) {
                    strings[i] = context.findStringByID((int) reader.readVarUint());
                }
                return strings;
            case SLICE_SYMBOL:
                String[] symbols = new String[size];
                for (int i = 0; i < size; i++) {
                    int dataId = (int) reader.readVarUint();
                    symbols[i] = stream ? context.findSymbolByID(dataId) : context.findStringByID(dataId);
                }
                return symbols;
            case SLICE_ARRAY:
                Object[] array = new Object[size];
                for (int i = 0; i < size; i++) {
                    array[i] = readArray(reader.readVarUint());
                }
                return array;
            case SLICE_OBJECT:
                Object[] objects = new Object[size];
                int structId = (int) reader.readVarUint();
                String[] fieldNames = context.findStructByID(structId);
                for (int i = 0; i < size; i++) {
                    Map<String, Object> obj = new HashMap<>();
                    for (String field : fieldNames) {
                        obj.put(field, readNode());
                    }
                    objects[i] = obj;
                }
                return objects;
            default:
                throw new InvalidReadException("run into invalid slice type: " + type);
        }
    }

}
