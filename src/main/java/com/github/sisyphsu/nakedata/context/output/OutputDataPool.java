package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.common.Array;
import com.github.sisyphsu.nakedata.context.common.IDAllocator;
import com.github.sisyphsu.nakedata.utils.ArrayUtils;
import com.github.sisyphsu.nakedata.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sulin
 * @since 2019-10-08 20:19:59
 */
public final class OutputDataPool {

    private final Array<Float>  tmpFloats  = new Array<>(true);
    private final Array<Double> tmpDoubles = new Array<>(true);
    private final Array<Long>   tmpVarints = new Array<>(true);
    private final Array<String> tmpStrings = new Array<>(true);

    private final Array<String> cxtSymbolAdded   = new Array<>(false);
    private final Array<String> cxtSymbolExpired = new Array<>(false);

    private final int                  cxtSymbolLimit;
    private final IDAllocator          cxtSymbolID    = new IDAllocator();
    private final Map<String, Integer> cxtSymbolIndex = new HashMap<>();
    private       String[]             cxtSymbols;
    private       Integer[]            cxtSymbolTime;

    public OutputDataPool(int cxtSymbolLimit) {
        this.cxtSymbolLimit = cxtSymbolLimit;
    }

    public void registerFloat(float f) {
        tmpFloats.add(f);
    }

    public void registerDouble(double d) {
        tmpDoubles.add(d);
    }

    public void registerVarint(long l) {
        tmpVarints.add(l);
    }

    public void registerString(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        tmpStrings.add(str);
    }

    public void registerSymbol(String symbol) {
        if (symbol == null) {
            throw new NullPointerException();
        }
        Integer offset = cxtSymbolIndex.get(symbol);
        if (offset == null) {
            offset = cxtSymbolID.acquire();
            this.cxtSymbols = ArrayUtils.put(cxtSymbols, offset, symbol);
            this.cxtSymbolAdded.add(symbol);
        }
        this.cxtSymbolTime = ArrayUtils.put(cxtSymbolTime, offset, (int) TimeUtils.fastUpTime());
    }

    public int findFloatID(float f) {
        Integer offset = tmpFloats.offset(f);
        if (offset == null) {
            throw new IllegalArgumentException("float not exists: " + f);
        }
        return 3 + offset;
    }

    public int findDoubleID(double d) {
        Integer offset = tmpDoubles.offset(d);
        if (offset == null) {
            throw new IllegalArgumentException("double not exists: " + d);
        }
        return 3 + tmpFloats.size() + offset;
    }

    public int findVarintID(long l) {
        Integer offset = tmpVarints.offset(l);
        if (offset == null) {
            throw new IllegalArgumentException("varint not exists: " + l);
        }
        return 3 + tmpFloats.size() + tmpDoubles.size() + offset;
    }

    public int findStringID(String str) {
        Integer offset = tmpStrings.offset(str);
        if (offset == null) {
            throw new IllegalArgumentException("string not exists: " + str);
        }
        return 3 + tmpFloats.size() + tmpDoubles.size() + tmpVarints.size() + offset;
    }

    public int findSymbolID(String symbol) {
        Integer offset = cxtSymbolIndex.get(symbol);
        if (offset == null) {
            throw new IllegalArgumentException("symbol not exists: " + symbol);
        }
        return 3 + tmpFloats.size() + tmpDoubles.size() + tmpVarints.size() + tmpStrings.size() + offset;
    }

    public void reset() {
        tmpFloats.clear();
        tmpDoubles.clear();
        tmpVarints.clear();
        tmpStrings.clear();
        cxtSymbolAdded.clear();
        cxtSymbolExpired.clear();
        // expire
        if (cxtSymbolIndex.size() > cxtSymbolLimit) {
            this.execRelease(cxtSymbolIndex.size() - cxtSymbolLimit);
        }
    }

    void execRelease(int count) {
        long[] heap = new long[count];

        // 0 means init, 1 means stable, -1 means not-stable.
        int heapStatus = 0;
        for (int itemOffset = 0, heapOffset = 0; itemOffset < cxtSymbols.length; itemOffset++) {
            if (cxtSymbols[itemOffset] == null) {
                continue;
            }
            int itemTime = cxtSymbolTime[itemOffset];
            if (heapOffset < count) {
                heap[heapOffset++] = ((long) itemTime) << 32 | (long) itemOffset;
                continue;
            }
            if (heapStatus == 0) {
                ArrayUtils.descFastSort(heap, 0, count - 1); // sort by activeTime, heap[0] has biggest activeTime
                heapStatus = 1;
            } else if (heapStatus == -1) {
                ArrayUtils.maxHeapAdjust(heap, 0, count); // make sure heap[0] has biggest activeTime
                heapStatus = 1;
            }
            if (itemTime > (int) (heap[0] >>> 32)) {
                continue; // item is newer than all items in heap
            }
            heap[0] = ((long) itemTime) << 32 | (long) itemOffset;
            heapStatus = -1;
        }

        // release all items in heap
        for (long l : heap) {
            int offset = (int) (l);
            String expiredSymbol = cxtSymbols[offset];
            this.cxtSymbolIndex.remove(expiredSymbol);
            this.cxtSymbolID.release(offset);
            this.cxtSymbols[offset] = null;

            this.cxtSymbolExpired.add(expiredSymbol);
        }
    }

}
