package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.io.InputReader;
import com.github.sisyphsu.nakedata.io.OutputWriter;
import lombok.Getter;

import java.util.List;

/**
 * 上下文自定义数据类型
 *
 * @author sulin
 * @since 2019-04-29 13:10:56
 */
@Getter
public class ContextType {

    /**
     * 类型ID
     */
    private int id;
    /**
     * 属性名称列表, 输入端使用
     */
    private int[] names;
    /**
     * 属性类型列表, 输入端使用
     */
    private int[] types;
    /**
     * Whether is temperary type or not
     */
    private boolean temp;
    /**
     * The fields of this Type, ordered and nullable.
     */
    private List<ContextField> fields;

    public ContextType() {
    }

    public ContextType(int id, List<ContextField> fields) {
        this.id = id;
        this.fields = fields;
    }

    public void doWrite(OutputWriter writer) {
        writer.writeUint(fields.size());
    }

    public void doRead(InputReader reader) {

    }

}
