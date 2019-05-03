package com.github.sisyphsu.nakedata.context;

import lombok.Getter;

/**
 * 上下文中名称元数据
 *
 * @author sulin
 * @since 2019-04-29 13:17:31
 */
@Getter
public class ContextName {

    /**
     * The unique id of name in context.
     */
    private final int id;
    /**
     * Real name stored in global.
     */
    private final String name;

    public ContextName(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContextName) {
            return this.name.equals(((ContextName) obj).name);
        }
        return false;
    }

}
