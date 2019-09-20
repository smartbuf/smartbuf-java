package com.github.sisyphsu.nakedata.context.input;

import com.github.sisyphsu.nakedata.utils.IDAllocator;

import java.util.List;

/**
 * Maintain type pool for input side.
 *
 * @author sulin
 * @since 2019-04-30 18:06:53
 */
public class InputTypePool {

    /**
     * The pool size of this TypePool, it mainly used for memory attack.
     */
    private final int limit;
    /**
     * The IDPool which used for id allocation, it should work exactly same with output-side.
     */
    private IDAllocator pool;
    /**
     * The types received from output-side.
     */
    private CType[] table;

    /**
     * Initialize type pool for input side.
     *
     * @param limit The pool size.
     */
    public InputTypePool(int limit) {
        this.limit = limit;
        this.pool = new IDAllocator(limit);
        this.table = new CType[64];
    }

    /**
     * Add the specified type by id, which was added by output-side.
     *
     * @param fields Fields of new type.
     */
    public void addType(List<ContextField> fields) {
        int id = this.pool.acquire();
        CType type = new CType(id, fields);
        // Expansion if needed
        if (id >= this.table.length && id < limit) {
            CType[] newTable = new CType[table.length * 2];
            System.arraycopy(table, 0, newTable, 0, table.length);
            this.table = newTable;
        }
        this.table[id] = type;
    }

    /**
     * Delete the specified type by id, which was expired by output-side.
     *
     * @param id type-id
     */
    public void delType(int id) {
        if (this.table.length <= id) {
            throw new IllegalArgumentException("invalid type: " + id);
        }
        this.pool.release(id);
        this.table[id] = null;
    }

    /**
     * Get the type from the specified id.
     *
     * @param id type-id.
     * @return customized type.
     */
    public CType getType(int id) {
        if (this.table.length <= id) {
            throw new IllegalArgumentException("invalid type: " + id);
        }
        CType type = this.table[id];
        if (type == null) {
            throw new IllegalArgumentException("undefined type: " + id);
        }
        return type;
    }

    public static class CType {

        private int id;
        private List<ContextField> fields;

        public CType(int id, List<ContextField> fields) {
            this.id = id;
            this.fields = fields;
        }

    }

}
