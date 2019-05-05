package com.github.sisyphsu.nakedata.context.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * 上下文自定义数据类型
 *
 * @author sulin
 * @since 2019-04-29 13:10:56
 */
@Getter
@Setter
public class ContextType {

    /**
     * 类型ID
     */
    private int id;
    /**
     * 数据类型的类型列表, 必须与struct对应
     */
    private final int[] types;
    /**
     * 数据类型的内部结构
     */
    private final ContextStruct struct;

    public ContextType(int[] types, ContextStruct struct) {
        this(-1, types, struct);
    }

    public ContextType(int id, int[] types, ContextStruct struct) {
        this.id = id;
        this.types = types;
        this.struct = struct;
    }

    @Override
    public int hashCode() {
        int result = 31 + struct.getId();
        for (int type : types) {
            result = 31 * result + type;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContextType) {
            ContextType other = (ContextType) obj;
            if (this.struct.getId() != other.struct.getId()) {
                return false;
            }
            return Arrays.equals(this.types, other.types);
        }
        return false;
    }

}
