package com.github.sisyphsu.nakedata.context;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 上下文中名称元数据
 *
 * @author sulin
 * @since 2019-04-29 13:17:31
 */
@Data
@AllArgsConstructor
public class ContextName {

    /**
     * The unique id of name in context.
     */
    private final int id;
    /**
     * Real name stored in global.
     */
    private final String name;

}
