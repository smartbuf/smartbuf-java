package com.github.sisyphsu.canoe.transport;

import java.io.IOException;

import static com.github.sisyphsu.canoe.transport.Const.*;

/**
 * InputContext holds the state of input's context, it helps decompress data and metadata's reusing.
 *
 * @author sulin
 * @since 2019-10-14 11:00:08
 */
public final class InputDataPool {

    private final Array<Float>  floats  = new Array<>();
    private final Array<Double> doubles = new Array<>();
    private final Array<Long>   varints = new Array<>();
    private final Array<String> strings = new Array<>();

    private final IDAllocator   symbolID = new IDAllocator();
    private final Array<String> symbols  = new Array<>();

    /**
     * Execute synchronization for schema and metadata of context
     */
    public void read(InputBuffer buf) throws IOException {
        boolean hasMore = true;
        byte flag;
        while (hasMore) {
            long head = buf.readVarUint();
            int size = (int) (head >> 4);
            hasMore = (head & 0b0000_0001) == 1;
            flag = (byte) ((head >>> 1) & 0b0000_0111);
            switch (flag) {
                case DATA_FLOAT:
                    for (int i = 0; i < size; i++) {
                        floats.add(buf.readFloat());
                    }
                    break;
                case DATA_DOUBLE:
                    for (int i = 0; i < size; i++) {
                        doubles.add(buf.readDouble());
                    }
                    break;
                case DATA_VARINT:
                    for (int i = 0; i < size; i++) {
                        varints.add(buf.readVarInt());
                    }
                    break;
                case DATA_STRING:
                    for (int i = 0; i < size; i++) {
                        strings.add(buf.readString());
                    }
                    break;
                case DATA_SYMBOL_ADDED:
                    for (int i = 0; i < size; i++) {
                        String symbol = buf.readString();
                        int id = symbolID.acquire();
                        symbols.put(id, symbol);
                    }
                    break;
                case DATA_SYMBOL_EXPIRED:
                    for (int i = 0; i < size; i++) {
                        int id = (int) buf.readVarUint();
                        symbolID.release(id);
                        symbols.put(id, null);
                    }
                    break;
                default:
                    throw new RuntimeException("invalid flag: " + flag);
            }
        }
    }

    public float getFloat(int index) {
        if (index == 1) {
            return 0f;
        }
        return floats.get(index - 2);
    }

    public double getDouble(int index) {
        if (index == 1) {
            return 0.0;
        }
        return doubles.get(index - 2);
    }

    public long getVarint(int index) {
        if (index == 1) {
            return 0L;
        }
        return varints.get(index - 2);
    }

    /**
     * Find the specified string by its unique ID
     *
     * @param id String's unique ID
     * @return String's value
     */
    public String getString(int id) {
        if (id == 1) {
            return "";
        }
        return strings.get(id - 2);
    }

    /**
     * Find the specified symbol by its unique ID
     *
     * @param id Symbol's unique ID
     * @return Symbol's value
     */
    public String getSymbol(int id) {
        int dataId = id - 1;
        if (dataId >= symbols.cap()) {
            throw new IllegalArgumentException("invalid symbol Id: " + id);
        }
        String symbol = symbols.get(dataId);
        if (symbol == null) {
            throw new IllegalArgumentException("invalid symbol id: " + id);
        }
        return symbol;
    }

    public void reset() {
        this.floats.clear();
        this.doubles.clear();
        this.varints.clear();
        this.strings.clear();
    }

}
