package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.utils.TimeUtils;

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

    private final Array<Float>         floats    = new Array<>();
    private final Map<Float, Integer>  floatMap  = new HashMap<>();
    private final Array<Double>        doubles   = new Array<>();
    private final Map<Double, Integer> doubleMap = new HashMap<>();
    private final Array<Long>          varints   = new Array<>();
    private final Map<Long, Integer>   varintMap = new HashMap<>();
    private final Array<String>        strings   = new Array<>();
    private final Map<String, Integer> stringMap = new HashMap<>();

    private final int                 symbolLimit;
    private final IDAllocator         symbolID      = new IDAllocator();
    private final Array<Symbol>       symbols       = new Array<>();
    private final Array<Symbol>       symbolAdded   = new Array<>();
    private final Array<Symbol>       symbolExpired = new Array<>();
    private final Map<String, Symbol> symbolIndex   = new HashMap<>();

    private int count;

    /**
     * Initialize DataPool, outter need specify the max number of symbol-area
     *
     * @param symbolLimit Max number of symbols, only for context
     */
    OutputDataPool(int symbolLimit) {
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
        Integer id = floatMap.get(f);
        if (id == null) {
            id = floats.add(f);
            floatMap.put(f, id);
        }
        return id + 2;
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
        Integer id = doubleMap.get(d);
        if (id == null) {
            id = doubles.add(d);
            doubleMap.put(d, id);
        }
        return id + 2;
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
        Integer id = varintMap.get(l);
        if (id == null) {
            id = varints.add(l);
            varintMap.put(l, id);
        }
        return id + 2;
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
        Integer id = stringMap.get(str);
        if (id == null) {
            id = strings.add(str);
            stringMap.put(str, id);
        }
        return id + 2;
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
        Symbol symbol = symbolIndex.get(str);
        if (symbol == null) {
            int index = symbolID.acquire();
            symbol = new Symbol(str, index);
            this.symbols.put(index, symbol);
            this.symbolAdded.add(symbol);
            this.symbolIndex.put(str, symbol);
        }
        symbol.lastTime = (int) TimeUtils.fastUpTime();
        return symbol.index + 1;
    }

    public boolean preOutput() {
        int count = 0;
        if (floats.size() > 0) count++;
        if (doubles.size() > 0) count++;
        if (varints.size() > 0) count++;
        if (strings.size() > 0) count++;
        if (symbolAdded.size() > 0) count++;
        if (symbolExpired.size() > 0) count++;
        this.count = count;
        return count > 0;
    }

    public boolean needSequence() {
        return symbolAdded.size() > 0 || symbolExpired.size() > 0;
    }

    public void write(OutputBuffer buf) {
        int len;
        if ((len = floats.size()) > 0) {
            buf.writeVarUint((len << 4) | (DATA_FLOAT << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeFloat(floats.get(i));
            }
        }
        if (count > 0 && (len = doubles.size()) > 0) {
            buf.writeVarUint((len << 4) | (DATA_DOUBLE << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeDouble(doubles.get(i));
            }
        }
        if (count > 0 && (len = varints.size()) > 0) {
            buf.writeVarUint((len << 4) | (DATA_VARINT << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeVarInt(varints.get(i));
            }
        }
        if (count > 0 && (len = strings.size()) > 0) {
            buf.writeVarUint((len << 4) | (DATA_STRING << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeString(strings.get(i));
            }
        }
        if (count > 0 && (len = symbolAdded.size()) > 0) {
            buf.writeVarUint((len << 4) | (DATA_SYMBOL_ADDED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeString(symbolAdded.get(i).value);
            }
        }
        if (count > 0 && (len = symbolExpired.size()) > 0) {
            buf.writeVarUint((len << 4) | (DATA_SYMBOL_EXPIRED << 1));
            for (int i = 0; i < len; i++) {
                buf.writeVarUint(symbolExpired.get(i).index);
            }
        }
    }

    /**
     * Reset this data pool, execute context data's expiring automatically
     */
    public void reset() {
        this.floats.clear();
        this.floatMap.clear();
        this.doubles.clear();
        this.doubleMap.clear();
        this.varints.clear();
        this.varintMap.clear();
        this.strings.clear();
        this.stringMap.clear();
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
            this.symbolExpired.add(expiredSymbol);
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
