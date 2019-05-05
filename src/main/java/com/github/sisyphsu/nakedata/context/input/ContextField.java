package com.github.sisyphsu.nakedata.context.input;

import com.github.sisyphsu.nakedata.context.model.ContextName;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.io.OutputWriter;
import lombok.Getter;

/**
 * @author sulin
 * @since 2019-04-29 13:14:20
 */
@Getter
@Deprecated
public class ContextField {

    /**
     * Field's name
     */
    private ContextName name;
    /**
     * Field's type
     */
    private ContextType type;

    public void doWrite(OutputWriter writer) {

    }

}
