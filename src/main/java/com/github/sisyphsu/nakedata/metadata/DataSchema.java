package com.github.sisyphsu.nakedata.metadata;

import java.util.List;

/**
 * 数据元数据模型
 * string, number, object, array, boolean
 *
 * @author sulin
 * @since 2019-03-21 20:14:26
 */
public class DataSchema {

    /**
     * 数据名称
     */
    private String name;
    /**
     * 数据类型
     * 标准数据: string, number, boolean等. object
     */
    private int type;

    private DataSchema arrayKey;

    /**
     * 哈希表的KEY类型
     */
    private DataSchema mapKey;
    /**
     * 哈希表的值类型
     */
    private DataSchema mapVal;

}
