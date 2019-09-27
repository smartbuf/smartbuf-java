package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.context.model.ContextName;

import java.util.List;

/**
 * @author sulin
 * @since 2019-04-29 13:19:50
 */
public class Context {

    /**
     * The incremental version of Context.
     */
    private int               version;
    /**
     * The max-count of type.
     */
    private int               typeLimit;
    /**
     * Customized field-name table.
     */
    private List<ContextName> nameTable;

    /**
     * 添加上下文类型, 用于支持标准bean
     */
    public void addType() {

    }

    /**
     * 添加临时类型, 用于支持动态map
     */
    public void addTmpType() {

    }

}
