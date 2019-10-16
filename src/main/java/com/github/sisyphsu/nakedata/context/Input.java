package com.github.sisyphsu.nakedata.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.sisyphsu.nakedata.context.Proto.*;

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

    public Input(InputStream inputStream, boolean stream) {
        this.stream = stream;
        this.schema = new Schema(stream);
        this.reader = new InputReader(inputStream);
        this.context = new InputContext(schema);
    }

    public Object read() throws IOException {
        schema.reset();
        schema.read(reader);
        // valid schema
        if (schema.stream != stream) {
            throw new RuntimeException("invalid mode");
        }
        if (schema.hasCxtMeta) {
            if ((schema.sequence & 0xFF) != ((sequence + 1) & 0xFF)) {
                throw new RuntimeException("invalid sequence");
            }
            this.sequence++;
        }
        // sync context
        this.context.sync();

        // load data
        return readNode();
    }

    private Object readNode() throws IOException {
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
                throw new RuntimeException("");
        }
    }

    /**
     * Read an array
     */
    private Object readArray(long head) throws IOException {
        boolean hasMore = (head & 1) == 1;
        if (!hasMore) {
            return readPureArray(head);
        }
        List<Object[]> slices = new ArrayList<>();
        int totalSize = 0;
        for (; hasMore; hasMore = (head & 1) == 1) {
            byte type = (byte) ((head >>> 1) & 0x0F);
            int size = (int) (head >>> 5);
            Object[] slice;
            totalSize += size;
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
                        slice[i] = context.findSymbolByID((int) reader.readVarUint());
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
                    throw new IllegalArgumentException("invalid data");
            }
            head = reader.readVarUint();
            slices.add(slice);
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
    private Object readPureArray(long head) throws IOException {
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
                    symbols[i] = context.findSymbolByID((int) reader.readVarUint());
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
                throw new IllegalArgumentException("invalid data");
        }
    }

}
