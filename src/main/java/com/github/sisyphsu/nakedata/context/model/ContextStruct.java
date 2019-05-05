package com.github.sisyphsu.nakedata.context.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * 上下文复用的类型结构, 封装变量名列表
 *
 * @author sulin
 * @since 2019-05-03 12:28:54
 */
@Getter
@Setter
public class ContextStruct {

    /**
     * 结构ID
     */
    private int id;
    /**
     * 结构内变量名列表, 应该是字母排序的
     */
    private final int[] nameIds;

    public ContextStruct(int[] nameIds) {
        this(-1, nameIds);
    }

    public ContextStruct(int id, int[] nameIds) {
        this.id = id;
        this.nameIds = nameIds;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int nameId : nameIds) {
            result = 31 * result + nameId;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ContextStruct)) {
            return false;
        }
        return Arrays.equals(this.nameIds, ((ContextStruct) obj).nameIds);
    }

}
