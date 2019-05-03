package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.io.InputReader;
import com.github.sisyphsu.nakedata.io.OutputWriter;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 上下文自定义数据类型
 *
 * @author sulin
 * @since 2019-04-29 13:10:56
 */
@Data
@AllArgsConstructor
public class ContextType {

    /**
     * 类型ID
     */
    private final int id;
    /**
     * 数据类型的类型列表, 必须与struct对应
     */
    private final int[] types;
    /**
     * 数据类型的内部结构
     */
    private final ContextStruct struct;

    public void doWrite(OutputWriter writer) {

    }

    public void doRead(InputReader reader) {

    }

}
