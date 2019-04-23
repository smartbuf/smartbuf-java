package com.github.sisyphsu.nakedata.metadata;

import java.util.List;

/**
 * 自定义对象的数据格式
 *
 * @author sulin
 * @since 2019-03-21 20:57:47
 */
public class ObjectDataSchema extends DataSchema {

    /**
     * 对象数据类型的字段列表, 有序数组
     */
    private List<DataField> fields;

}
