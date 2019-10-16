package com.github.sisyphsu.nakedata.proto;

import static com.github.sisyphsu.nakedata.proto.Const.*;

/**
 * InputContext holds the state of input's context, it helps decompress data and metadata's reusing.
 *
 * @author sulin
 * @since 2019-10-14 11:00:08
 */
public final class InputContext {

    private final Schema schema;

    private final Array<Float>    tmpFloats;
    private final Array<Double>   tmpDoubles;
    private final Array<Long>     tmpVarints;
    private final Array<String>   tmpStrings;
    private final Array<String>   tmpNames;
    private final Array<String[]> tmpStructs = new Array<>();

    private final IDAllocator   cxtNameId = new IDAllocator();
    private final Array<String> cxtNames  = new Array<>();

    private final IDAllocator     cxtStructID = new IDAllocator();
    private final Array<String[]> cxtStructs  = new Array<>();

    private final IDAllocator   cxtSymbolID = new IDAllocator();
    private final Array<String> cxtSymbols  = new Array<>();

    /**
     * Initalize InputContext, caller should provide {@link Schema} for synchronization of schema and metadata.
     *
     * @param schema Schema of input side
     */
    public InputContext(Schema schema) {
        this.schema = schema;
        this.tmpFloats = schema.tmpFloats;
        this.tmpDoubles = schema.tmpDoubles;
        this.tmpVarints = schema.tmpVarints;
        this.tmpStrings = schema.tmpStrings;
        this.tmpNames = schema.tmpNames;
    }

    /**
     * Execute synchronization for schema and metadata of context
     */
    public void sync() {
        this.tmpStructs.clear();

        int len;
        // accept expires
        len = schema.cxtNameExpired.size();
        for (int i = 0; i < len; i++) {
            cxtNameId.release(schema.cxtNameExpired.get(i));
        }
        len = schema.cxtSymbolExpired.size();
        for (int i = 0; i < len; i++) {
            cxtSymbolID.release(schema.cxtSymbolExpired.get(i));
        }
        len = schema.cxtStructExpired.size();
        for (int i = 0; i < len; i++) {
            cxtStructID.release(schema.cxtStructExpired.get(i));
        }
        // accept add
        len = schema.cxtSymbolAdded.size();
        for (int i = 0; i < len; i++) {
            cxtSymbols.add(schema.cxtSymbolAdded.get(i));
        }
        len = schema.cxtNameAdded.size();
        for (int i = 0; i < len; i++) {
            cxtNames.add(schema.cxtNameAdded.get(i));
        }
        len = schema.cxtStructAdded.size();
        for (int i = 0; i < len; i++) {
            int[] nameIds = schema.cxtStructAdded.get(i);
            String[] fields = new String[nameIds.length];
            for (int j = 0; j < nameIds.length; j++) {
                fields[j] = findNameByID(nameIds[j]);
            }
            cxtStructs.add(fields);
        }
    }

    /**
     * Fetch the specified name by its unique id
     *
     * @param id The unique id
     * @return name
     */
    public String findNameByID(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("negative id: " + id);
        }
        if (id < tmpNames.size()) {
            return tmpNames.get(id);
        }
        id -= tmpNames.size();
        if (id > cxtNameId.count()) {
            throw new IllegalArgumentException("invalid id: " + id);
        }
        String result = cxtNames.get(id);
        if (result == null) {
            throw new IllegalArgumentException("invalid id: " + id);
        }
        return result;
    }

    /**
     * Find the specified struct's fields by its unique id
     *
     * @param id Struct's unique ID
     * @return Struct's fields
     */
    public String[] findStructByID(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("negative id: " + id);
        }
        if (id < tmpStructs.size()) {
            return tmpStructs.get(id);
        }
        id -= tmpStructs.size();
        if (id > cxtStructs.size()) {
            throw new IllegalArgumentException("invalid id: " + id);
        }
        String[] struct = cxtStructs.get(id);
        if (struct == null) {
            throw new IllegalArgumentException("invalid id: " + id);
        }
        return struct;
    }

    /**
     * Find the specified string by its unique ID
     *
     * @param id String's unique ID
     * @return String's value
     */
    public String findStringByID(int id) {
        id -= ID_PREFIX;
        id -= tmpFloats.size();
        id -= tmpDoubles.size();
        id -= tmpVarints.size();
        if (id < tmpStrings.size()) {
            return tmpStrings.get(id);
        }
        throw new IllegalArgumentException("invalid string dataId: " + id);
    }

    /**
     * Find the specified symbol by its unique ID
     *
     * @param id Symbol's unique ID
     * @return Symbol's value
     */
    public String findSymbolByID(int id) {
        id -= ID_PREFIX;
        id -= tmpFloats.size();
        id -= tmpDoubles.size();
        id -= tmpVarints.size();
        id -= tmpStrings.size();
        if (id >= cxtSymbols.cap()) {
            throw new IllegalArgumentException("invalid string dataId: " + id);
        }
        String symbol = cxtSymbols.get(id);
        if (symbol == null) {
            throw new IllegalArgumentException("invalid string dataId: " + id);
        }
        return symbol;
    }

    /**
     * Find the specified data by its unique id, includ float/double/varint/string/symbol
     *
     * @param id Data's unique ID
     * @return Data's value
     */
    public Object findDataByID(int id) {
        int offset = id;
        switch (offset) {
            case ID_NULL:
                return null;
            case ID_TRUE:
                return true;
            case ID_FALSE:
                return false;
        }
        int len;

        offset -= ID_PREFIX;
        if (offset < (len = tmpFloats.size())) {
            return tmpFloats.get(offset);
        }
        offset -= len;
        if (offset < (len = tmpDoubles.size())) {
            return tmpDoubles.get(offset);
        }
        offset -= len;
        if (offset < (len = tmpVarints.size())) {
            return tmpVarints.get(offset);
        }
        offset -= len;
        if (offset < (len = tmpStrings.size())) {
            return tmpStrings.get(offset);
        }
        offset -= len;
        if (offset >= cxtSymbols.cap()) {
            throw new IllegalArgumentException("invalid data-id: " + id);
        }
        String symbol = cxtSymbols.get(offset);
        if (symbol == null) {
            throw new IllegalArgumentException("invalid data-id: " + id);
        }
        return symbol;
    }

}
