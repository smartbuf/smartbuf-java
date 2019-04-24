package com.github.sisyphsu.nakedata.field;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sulin
 * @since 2019-04-24 20:52:54
 */
public class FieldNameIndex {

    private Map<String, Integer> nameIdMap = new HashMap<>();
    private Map<Integer, String> idNameMap = new HashMap<>();

    /**
     * Fetch id of the specified name
     *
     * @param name Field's name
     * @return unique ID
     */
    public synchronized Integer getID(String name) {
        return this.nameIdMap.get(name);
    }

    /**
     * Fetch name of the specified fieldID
     *
     * @param id Field's id
     * @return name
     */
    public synchronized String getName(int id) {
        return this.idNameMap.get(id);
    }

    /**
     * Add the specified field's name into index, if it's new
     *
     * @param name Field's name
     * @return id, null if it isn't new.
     */
    public synchronized Integer addNameIfNew(String name) {
        if (this.nameIdMap.containsKey(name)) {
            return null;
        }
        int id = nameIdMap.size();
        this.nameIdMap.put(name, id);
        this.idNameMap.put(id, name);
        return id;
    }

}
