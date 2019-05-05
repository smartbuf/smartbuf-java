package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.ContextName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 输出端通用的StructKey
 *
 * @author sulin
 * @since 2019-05-05 11:12:11
 */
@Getter
@AllArgsConstructor
public class OutputStructKey {

    private final ContextName[] names;

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
        if (!(obj instanceof OutputStructKey)) {
            return false;
        }
        OutputStructKey other = (OutputStructKey) obj;
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
