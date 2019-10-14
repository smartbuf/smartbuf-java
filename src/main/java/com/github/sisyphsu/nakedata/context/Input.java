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
        nodeID = nodeID >>> 2;
        switch (flag) {
            case FLAG_DATA:
                return context.findDataByID((int) nodeID);
            case FLAG_ARRAY:
                return this.readArray(nodeID >>> 2);
            case FLAG_STRUCT:
                String[] fields = context.findStructByID((int) nodeID);
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
        List<Object> result = new ArrayList<>();
        for (; hasMore; hasMore = (head & 1) == 1) {
            byte type = (byte) ((head >>> 1) & 0x0F);
            int size = (int) (head >>> 5);
            switch (type) {
                case SLICE_NULL:
                    for (int i = 0; i < size; i++) {
                        result.add(null);
                    }
                    break;
                case SLICE_BOOL:
                    reader.readBooleanSlice(result, size);
                    break;
                case SLICE_BYTE:
                    reader.readByteSlice(result, size);
                    break;
                case SLICE_SHORT:
                    reader.readShortSlice(result, size);
                    break;
                case SLICE_INT:
                    reader.readIntSlice(result, size);
                    break;
                case SLICE_LONG:
                    reader.readLongSlice(result, size);
                    break;
                case SLICE_FLOAT:
                    reader.readFloatSlice(result, size);
                    break;
                case SLICE_DOUBLE:
                    reader.readDoubleSlice(result, size);
                    break;
                case SLICE_STRING:
                    for (int i = 0; i < size; i++) {
                        int dataId = (int) reader.readVarUint();
                        result.add(context.findDataByID(dataId));
                    }
                    break;
                case SLICE_ARRAY:
                    for (int i = 0; i < size; i++) {
                        long subHead = reader.readVarUint();
                        result.add(readArray(subHead));
                    }
                    break;
                case SLICE_OBJECT:
                    int structId = (int) reader.readVarUint();
                    String[] fieldNames = context.findStructByID(structId);
                    for (int i = 0; i < size; i++) {
                        Map<String, Object> obj = new HashMap<>();
                        for (String field : fieldNames) {
                            obj.put(field, readNode());
                        }
                        result.add(obj);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("invalid data");
            }
            head = reader.readVarUint();
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
                String[] arr = new String[size];
                for (int i = 0; i < size; i++) {
                    int dataId = (int) reader.readVarUint();
                    arr[i] = (String) context.findDataByID(dataId);
                }
                return arr;
            case SLICE_ARRAY:
                Object[] array = new Object[size];
                for (int i = 0; i < size; i++) {
                    array[i] = readNode();
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
