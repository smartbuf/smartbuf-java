package com.github.sisyphsu.nakedata.context;

import static com.github.sisyphsu.nakedata.context.Proto.*;

/**
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

    public InputContext(Schema schema) {
        this.schema = schema;
        this.tmpFloats = schema.tmpFloats;
        this.tmpDoubles = schema.tmpDoubles;
        this.tmpVarints = schema.tmpVarints;
        this.tmpStrings = schema.tmpStrings;
        this.tmpNames = schema.tmpNames;
    }

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

    public float findFloatByID(int id) {
        return 0;
    }

    public double findDoubleByID(int id) {
        return 0;
    }

    public long findVarintByID(int id) {
        return 0;
    }

    public String findStringByID(int id) {
        return null;
    }

    public String findSymbolByID(int id) {
        return null;
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
            return tmpDoubles.size();
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
        if (offset < cxtSymbols.size()) {
            return cxtSymbols.get(offset);
        }
        throw new IllegalArgumentException("invalid data-id: " + id);
    }

}
