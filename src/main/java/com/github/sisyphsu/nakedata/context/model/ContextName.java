package com.github.sisyphsu.nakedata.context.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 上下文中名称元数据
 *
 * @author sulin
 * @since 2019-04-29 13:17:31
 */
@Getter
@Setter
public class ContextName {

    /**
     * The unique id of name in context.
     */
    private int id;
    /**
     * Real name stored in global.
     */
    private final String name;

    public ContextName(String name) {
        this(-1, name);
    }

    public ContextName(int id, String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("ContextName's name can't be null or empty");
        }
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContextName) {
            return this.name.equals(((ContextName) obj).name);
        }
        return false;
    }

}
