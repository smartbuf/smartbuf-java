package com.github.sisyphsu.nakedata.context;

import lombok.Getter;

/**
 * 上下文复用的类型结构, 封装变量名列表
 *
 * @author sulin
 * @since 2019-05-03 12:28:54
 */
@Getter
public class ContextStruct {

    /**
     * 结构ID
     */
    private int id;
    /**
     * 结构内变量名列表, 应该是字母排序的
     */
    private final ContextName[] names;

    public ContextStruct(ContextName[] names) {
        this(-1, names);
    }

    public ContextStruct(int id, ContextName[] names) {
        if (names == null || names.length <= 0) {
            throw new IllegalArgumentException("names can't be empty");
        }
        this.id = id;
        this.names = names;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (ContextName name : names) {
            result = 31 * result + name.getId();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ContextStruct)) {
            return false;
        }
        ContextStruct other = (ContextStruct) obj;
        if (this.names.length != other.names.length) {
            return false;
        }
        for (int i = 0; i < names.length; i++) {
            ContextName left = names[i];
            ContextName right = other.names[i];
            if (left.getId() != right.getId()) {
                return false;
            }
        }
        return true;
    }

}
