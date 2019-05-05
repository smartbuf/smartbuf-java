package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.ContextStruct;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 输出端用作map-key的数据类型Key
 *
 * @author sulin
 * @since 2019-05-05 11:13:14
 */
@Getter
@AllArgsConstructor
public class OutputTypeKey {

    private final int[] types;
    private final ContextStruct struct;

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
        if (obj instanceof OutputTypeKey) {
            OutputTypeKey other = (OutputTypeKey) obj;
            if (this.struct.getId() != other.struct.getId()) {
                return false;
            }
            return Arrays.equals(this.types, other.types);
        }
        return false;
    }

}
