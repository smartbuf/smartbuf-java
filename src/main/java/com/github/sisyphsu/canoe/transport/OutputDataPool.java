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

    private final int         symbolLimit;
    private final IDAllocator symbolID = new IDAllocator();

    final Array<Float>         tmpFloats    = new Array<>();
    final Map<Float, Integer>  tmpFloatMap  = new HashMap<>();
    final Array<Double>        tmpDoubles   = new Array<>();
    final Map<Double, Integer> tmpDoubleMap = new HashMap<>();
    final Array<Long>          tmpVarints   = new Array<>();
    final Map<Long, Integer>   tmpVarintMap = new HashMap<>();
    final Array<String>        tmpStrings   = new Array<>();
    final Map<String, Integer> tmpStringMap = new HashMap<>();

    final Array<Symbol>       symbols          = new Array<>();
    final Array<Symbol>       cxtSymbolAdded   = new Array<>();
    final Array<Symbol>       cxtSymbolExpired = new Array<>();
    final Map<String, Symbol> symbolIndex      = new HashMap<>();

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
            return ID_ZERO_FLOAT;
        }
        Integer id = tmpFloatMap.get(f);
        if (id == null) {
            id = tmpFloats.add(f);
            tmpFloatMap.put(f, id);
        }
        return id;
    }

    /**
     * Register the specified double data into double-area
     *
     * @param d Double data
     * @return Double ID
     */
    public int registerDouble(double d) {
        if (d == 0) {
            return ID_ZERO_DOUBLE;
        }
        Integer id = tmpDoubleMap.get(d);
        if (id == null) {
            id = tmpDoubles.add(d);
            tmpDoubleMap.put(d, id);
        }
        return id;
    }

    /**
     * Register the specified varint data into varint-area, which means long
     *
     * @param l Varint data
     * @return Varint ID
     */
    public int registerVarint(long l) {
        if (l == 0) {
            return ID_ZERO_VARINT;
        }
        Integer id = tmpVarintMap.get(l);
        if (id == null) {
            id = tmpVarints.add(l);
            tmpVarintMap.put(l, id);
        }
        return id;
    }

    /**
     * Register the specified string data into string-area
     *
     * @param str String data
     * @return String ID
     */
    public int registerString(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        if (str.isEmpty()) {
            return ID_ZERO_STRING;
        }
        Integer id = tmpStringMap.get(str);
        if (id == null) {
            id = tmpStrings.add(str);
            tmpStringMap.put(str, id);
        }
        return id;
    }

    /**
     * Register the specified symbol into symbol-area, symbol is a sort of special strings.
     *
     * @param str Symbol data
     * @return Symbol ID
     */
    public int registerSymbol(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        Symbol symbol = symbolIndex.get(str);
        if (symbol == null) {
            int index = symbolID.acquire();
            symbol = new Symbol(str, index);
            this.symbols.put(index, symbol);
            this.cxtSymbolAdded.add(symbol);
            this.symbolIndex.put(str, symbol);
        }
        symbol.lastTime = (int) TimeUtils.fastUpTime();
        return symbol.index;
    }

    /**
     * Reset this data pool, execute context data's expiring automatically
     */
    public void reset() {
        this.tmpFloats.clear();
        this.tmpFloatMap.clear();
        this.tmpDoubles.clear();
        this.tmpDoubleMap.clear();
        this.tmpVarints.clear();
        this.tmpVarintMap.clear();
        this.tmpStrings.clear();
        this.tmpStringMap.clear();
        this.cxtSymbolAdded.clear();
        this.cxtSymbolExpired.clear();

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
            this.cxtSymbolExpired.add(expiredSymbol);
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
