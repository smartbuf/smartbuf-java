package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.utils.TimeUtils;

import java.io.IOException;
import java.util.*;

import static com.github.sisyphsu.canoe.transport.Const.*;

/**
 * DataPool represents an area holds data properties like float/double/varint/string/symbol.
 * <p>
 * It will allocate unique ID for every data object by its type and value.
 *
 * @author sulin
 * @since 2019-10-08 20:19:59
 */
public final class OutputDataPool {

    private static final byte HAS_FLOAT          = 1;
    private static final byte HAS_DOUBLE         = 1 << 1;
    private static final byte HAS_VARINT         = 1 << 2;
    private static final byte HAS_STRING         = 1 << 3;
    private static final byte HAS_SYMBOL_ADDED   = 1 << 4;
    private static final byte HAS_SYMBOL_EXPIRED = 1 << 5;

    private static final byte NEED_SEQ = HAS_SYMBOL_ADDED | HAS_SYMBOL_EXPIRED;

    private final Array<Float>         floats      = new Array<>();
    private final Map<Float, Integer>  floatIndex  = new HashMap<>();
    private final Array<Double>        doubles     = new Array<>();
    private final Map<Double, Integer> doubleIndex = new HashMap<>();
    private final Array<Long>          varints     = new Array<>();
    private final Map<Long, Integer>   varintIndex = new HashMap<>();
    private final Array<String>        strings     = new Array<>();
    private final Map<String, Integer> stringIndex = new HashMap<>();

    private final int                 symbolLimit;
    private final IDAllocator         symbolID      = new IDAllocator();
    private final Array<Symbol>       symbols       = new Array<>();
    private final Array<Symbol>       symbolAdded   = new Array<>();
    private final Array<Integer>      symbolExpired = new Array<>();
    private final Map<String, Symbol> symbolIndex   = new HashMap<>();

    private byte flags;

    /**
     * Initialize DataPool, outter need specify the max number of symbol-area
     *
     * @param symbolLimit Max number of symbols, only for context
     */
    public OutputDataPool(int symbolLimit) {
        this.symbolLimit = symbolLimit;
    }

    /**
     * Register the specified float data into float-area
     *
     * @param f Float data
     * @return FloatID
     */
    public int registerFloat(float f) {
        if (f == 0) {
            return 1;
        }
        return floatIndex.computeIfAbsent(f, floats::add) + 2;
    }

    /**
     * Register the specified double data into double-area
     *
     * @param d Double data
     * @return Double ID
     */
    public int registerDouble(double d) {
        if (d == 0) {
            return 1;
        }
        return doubleIndex.computeIfAbsent(d, doubles::add) + 2;
    }

    /**
     * Register the specified varint data into varint-area, which means long
     *
     * @param l Varint data
     * @return Varint ID
     */
    public int registerVarint(long l) {
        if (l == 0) {
            return 1;
        }
        return varintIndex.computeIfAbsent(l, varints::add) + 2;
    }

    /**
     * Register the specified string data into string-area
     *
     * @param str String data
     * @return String ID
     */
    public int registerString(String str) {
        if (str.isEmpty()) {
            return 1;
        }
        return stringIndex.computeIfAbsent(str, strings::add) + 2;
    }

    /**
     * Register the specified symbol into symbol-area, symbol is a sort of special strings.
     *
     * @param str Symbol data
     * @return Symbol ID
     */
    public int registerSymbol(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("invalid symbol: " + str);
        }
        Symbol symbol = symbolIndex.computeIfAbsent(str, s -> {
            int index = symbolID.acquire();
            Symbol result = new Symbol(str, index);
            this.symbols.put(index, result);
            this.symbolAdded.add(result);
            return result;
        });
        symbol.lastTime = (int) TimeUtils.fastUpTime();
        return symbol.index + 1;
    }

    public boolean needOutput() {
        byte flags = 0;
        if (floats.size() > 0) flags |= HAS_FLOAT;
        if (doubles.size() > 0) flags |= HAS_DOUBLE;
        if (varints.size() > 0) flags |= HAS_VARINT;
        if (strings.size() > 0) flags |= HAS_STRING;
        if (symbolAdded.size() > 0) flags |= HAS_SYMBOL_ADDED;
        if (symbolExpired.size() > 0) flags |= HAS_SYMBOL_EXPIRED;
        this.flags = flags;
        return flags != 0;
    }

    public boolean needSequence() {
        return (flags & NEED_SEQ) != 0;
    }

    public void write(OutputBuffer buf) throws IOException {
        byte flags = this.flags;
        int len;
        if ((flags & HAS_FLOAT) != 0) {
            len = floats.size();
            flags ^= HAS_FLOAT;
            buf.writeVarUint((len << 4) | DATA_FLOAT | (flags == 0 ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeFloat(floats.get(i));
            }
        }
        if ((flags & HAS_DOUBLE) != 0) {
            len = doubles.size();
            flags ^= HAS_DOUBLE;
            buf.writeVarUint((len << 4) | DATA_DOUBLE | (flags == 0 ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeDouble(doubles.get(i));
            }
        }
        if ((flags & HAS_VARINT) != 0) {
            len = varints.size();
            flags ^= HAS_VARINT;
            buf.writeVarUint((len << 4) | DATA_VARINT | (flags == 0 ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeVarInt(varints.get(i));
            }
        }
        if ((flags & HAS_STRING) != 0) {
            len = strings.size();
            flags ^= HAS_STRING;
            buf.writeVarUint((len << 4) | DATA_STRING | (flags == 0 ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeString(strings.get(i));
            }
        }
        if ((flags & HAS_SYMBOL_EXPIRED) != 0) {
            len = symbolExpired.size();
            flags ^= HAS_SYMBOL_EXPIRED;
            buf.writeVarUint((len << 4) | DATA_SYMBOL_EXPIRED | (flags == 0 ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeVarUint(symbolExpired.get(i));
            }
        }
        if ((flags & HAS_SYMBOL_ADDED) != 0) {
            len = symbolAdded.size();
            buf.writeVarUint((len << 4) | DATA_SYMBOL_ADDED);
            for (int i = 0; i < len; i++) {
                buf.writeString(symbolAdded.get(i).value);
            }
        }
    }

    /**
     * Reset this data pool, execute context data's expiring automatically
     */
    public void reset() {
        this.floats.clear();
        this.floatIndex.clear();
        this.doubles.clear();
        this.doubleIndex.clear();
        this.varints.clear();
        this.varintIndex.clear();
        this.strings.clear();
        this.stringIndex.clear();
        this.symbolAdded.clear();
        this.symbolExpired.clear();

        // check and expire symbols if thay are too many
        int expireNum = symbolIndex.size() - symbolLimit;
        if (expireNum <= 0) {
            return;
        }
        List<Symbol> symbols = new ArrayList<>(symbolIndex.values());
        symbols.sort(Comparator.comparingInt(s -> s.lastTime));
        for (int i = 0, len = Math.min(expireNum, symbols.size()); i < len; i++) {
            Symbol expiredSymbol = symbols.get(i);
            this.symbolIndex.remove(expiredSymbol.value);
            this.symbolID.release(expiredSymbol.index);
            this.symbols.put(expiredSymbol.index, null);
            this.symbolExpired.add(expiredSymbol.index);
        }
    }

    static class Symbol {
        String value;
        int    index;
        int    lastTime;

        public Symbol(String value, int index) {
            this.value = value;
            this.index = index;
        }
    }
}
