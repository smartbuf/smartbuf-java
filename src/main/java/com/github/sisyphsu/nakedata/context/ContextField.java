package com.github.sisyphsu.nakedata.context;

import lombok.Getter;

/**
 * @author sulin
 * @since 2019-04-29 13:14:20
 */
@Getter
public class ContextField {

    /**
     * Field's name
     */
    private ContextName name;
    /**
     * Field's type
     */
    private ContextType type;

}
