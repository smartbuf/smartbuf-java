package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.utils.ArrayUtils;
import com.github.sisyphsu.nakedata.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;

import static com.github.sisyphsu.nakedata.context.Proto.ID_PREFIX;

/**
 * DataPool represents an area holds data properties like float/double/varint/string/symbol.
 * <p>
 * It will allocate unique ID for every data object by its type and value.
 *
 * @author sulin
 * @since 2019-10-08 20:19:59
 */
public final class OutputDataPool {

    private final Array<Float>  tmpFloats;
    private final Array<Double> tmpDoubles;
    private final Array<Long>   tmpVarints;
    private final Array<String> tmpStrings;

    private final Map<Object, Integer> dataIndex = new HashMap<>();

    private final Array<String>  cxtSymbolAdded;
    private final Array<Integer> cxtSymbolExpired;

    private final int            symbolLimit;
    private final IDAllocator    symbolID    = new IDAllocator();
    private final Array<String>  symbols     = new Array<>();
    private final Array<Integer> symbolTimes = new Array<>();

    private final Map<String, Integer> symbolIndex = new HashMap<>();

    /**
     * Initialize DataPool, outter need specify the max number of symbol-area
     *
     * @param schema      Schema
     * @param symbolLimit Max number of symbols, only for context
     */
    OutputDataPool(Schema schema, int symbolLimit) {
        this.symbolLimit = symbolLimit;
        this.tmpFloats = schema.tmpFloats;
        this.tmpDoubles = schema.tmpDoubles;
        this.tmpVarints = schema.tmpVarints;
        this.tmpStrings = schema.tmpStrings;
        this.cxtSymbolAdded = schema.cxtSymbolAdded;
        this.cxtSymbolExpired = schema.cxtSymbolExpired;
    }

    /**
     * Register the specified float data into float-area
     *
     * @param f Float data
     */
    public void registerFloat(float f) {
        if (!dataIndex.containsKey(f)) {
            dataIndex.put(f, tmpFloats.add(f));
        }
    }

    /**
     * Register the specified double data into double-area
     *
     * @param d Double data
     */
    public void registerDouble(double d) {
        if (!dataIndex.containsKey(d)) {
            dataIndex.put(d, tmpDoubles.add(d));
        }
    }

    /**
     * Register the specified varint data into varint-area, which means long
     *
     * @param l Varint data
     */
    public void registerVarint(long l) {
        if (!dataIndex.containsKey(l)) {
            dataIndex.put(l, tmpVarints.add(l));
        }
    }

    /**
     * Register the specified string data into string-area
     *
     * @param str String data
     */
    public void registerString(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        if (!dataIndex.containsKey(str)) {
            dataIndex.put(str, tmpStrings.add(str));
        }
    }

    /**
     * Register the specified symbol into symbol-area, symbol is a sort of special strings.
     *
     * @param symbol Symbol data
     */
    public void registerSymbol(String symbol) {
        if (symbol == null) {
            throw new NullPointerException();
        }
        Integer offset = symbolIndex.get(symbol);
        if (offset == null) {
            offset = symbolID.acquire();
            this.symbols.put(offset, symbol);
            this.cxtSymbolAdded.add(symbol);
            this.symbolIndex.put(symbol, offset);
        }
        this.symbolTimes.put(offset, (int) TimeUtils.fastUpTime());
    }

    /**
     * Find unique id of the specified float data
     *
     * @param f The specified float data
     * @return Its unique ID
     */
    public int findFloatID(float f) {
        Integer offset = dataIndex.get(f);
        if (offset == null) {
            throw new IllegalArgumentException("float not exists: " + f);
        }
        return ID_PREFIX + offset;
    }

    /**
     * Find unique id of the specified double data
     *
     * @param d The specified double data
     * @return Its unique ID
     */
    public int findDoubleID(double d) {
        Integer offset = dataIndex.get(d);
        if (offset == null) {
            throw new IllegalArgumentException("double not exists: " + d);
        }
        return ID_PREFIX + tmpFloats.size() + offset;
    }

    /**
     * Find unique id of the specified long data
     *
     * @param l The specified long data
     * @return Its unique ID
     */
    public int findVarintID(long l) {
        Integer offset = dataIndex.get(l);
        if (offset == null) {
            throw new IllegalArgumentException("varint not exists: " + l);
        }
        return ID_PREFIX + tmpFloats.size() + tmpDoubles.size() + offset;
    }

    /**
     * Find unique id of the specified string
     *
     * @param str The specified string
     * @return Its unique ID
     */
    public int findStringID(String str) {
        Integer offset = dataIndex.get(str);
        if (offset == null) {
            throw new IllegalArgumentException("string not exists: " + str);
        }
        return ID_PREFIX + tmpFloats.size() + tmpDoubles.size() + tmpVarints.size() + offset;
    }

    /**
     * Find unique id of the specified symbol
     *
     * @param symbol The specified symbol
     * @return Its unique ID
     */
    public int findSymbolID(String symbol) {
        Integer offset = symbolIndex.get(symbol);
        if (offset == null) {
            throw new IllegalArgumentException("symbol not exists: " + symbol);
        }
        return ID_PREFIX + dataIndex.size() + offset;
    }

    /**
     * Fetch total number of struct in temporary and context area.
     *
     * @return Total number
     */
    public int size() {
        return dataIndex.size() + symbolIndex.size();
    }

    /**
     * Reset this data pool, execute context data's expiring automatically
     */
    public void reset() {
        dataIndex.clear();

        int expireNum = symbolIndex.size() - symbolLimit;
        if (expireNum <= 0) {
            return;
        }
        int heapStatus = 0; // 0 means init, 1 means stable, -1 means not-stable.
        int heapOffset = 0;
        long[] heap = new long[expireNum];
        for (int i = 0, size = symbols.cap(); i < size; i++) {
            if (symbols.get(i) == null) {
                continue;
            }
            int itemTime = symbolTimes.get(i);
            if (heapOffset < expireNum) {
                heap[heapOffset++] = ((long) itemTime) << 32 | (long) i;
                continue;
            }
            if (heapStatus == 0) {
                ArrayUtils.descFastSort(heap, 0, expireNum - 1); // sort by activeTime, heap[0] has biggest activeTime
                heapStatus = 1;
            } else if (heapStatus == -1) {
                ArrayUtils.maxHeapAdjust(heap, 0, expireNum); // make sure heap[0] has biggest activeTime
                heapStatus = 1;
            }
            if (itemTime > (int) (heap[0] >>> 32)) {
                continue; // item is newer than all items in heap
            }
            heap[0] = ((long) itemTime) << 32 | (long) i;
            heapStatus = -1;
        }

        for (long l : heap) {
            int offset = (int) (l);
            String expiredSymbol = symbols.get(offset);
            this.symbolIndex.remove(expiredSymbol);
            this.symbolID.release(offset);
            this.symbols.put(offset, null);

            this.cxtSymbolExpired.add(offset);
        }
    }

}