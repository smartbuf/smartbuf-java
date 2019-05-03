package com.github.sisyphsu.nakedata.context;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 上下文复用的类型结构, 封装变量名列表
 *
 * @author sulin
 * @since 2019-05-03 12:28:54
 */
@Data
@AllArgsConstructor
public class ContextStruct {

    /**
     * 结构ID
     */
    private final int id;
    /**
     * 结构内变量名列表, 应该是字母排序的
     */
    private final ContextName[] names;

}
