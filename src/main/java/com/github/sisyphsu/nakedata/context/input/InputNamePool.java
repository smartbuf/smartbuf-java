package com.github.sisyphsu.nakedata.context.input;

import com.github.sisyphsu.nakedata.common.IDPool;

/**
 * Context's name pool for input side.
 *
 * @author sulin
 * @since 2019-05-01 14:02:15
 */
public class InputNamePool {

    /**
     * The pool size, it mainly used for memory attack.
     */
    private final int limit;
    /**
     * The IDPool which used for id allocation, it should work exactly same with output-side.
     */
    private IDPool pool;
    /**
     * The real name
     */
    private String[] table;

    public InputNamePool(int limit) {
        this.limit = limit;
        this.pool = new IDPool(limit);
        this.table = new String[64];
    }

    /**
     * Get the name's value by id.
     *
     * @param id name's id.
     * @return name's value.
     */
    public String getName(int id) {
        return this.table[id];
    }

    /**
     * Add new name into this pool
     *
     * @param name name's value
     */
    public void addName(String name) {
        int id = pool.acquire();
        // Expansion if need
        if (this.table.length <= id) {
            String[] newTable = new String[Math.min(table.length * 2, limit)];
            System.arraycopy(table, 0, newTable, 0, table.length);
            this.table = newTable;
        }
        this.table[id] = name;
    }

    /**
     * Delete name by id
     *
     * @param id name's id
     */
    public void delName(int id) {
        pool.release(id);
        this.table[id] = null;
    }

}
