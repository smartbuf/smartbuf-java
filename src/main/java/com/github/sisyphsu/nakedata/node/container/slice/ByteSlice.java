package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sulin
 * @since 2019-05-08 21:01:31
 */
@Getter
@Setter
public class ByteSlice extends Node {

    public final static ByteArrayNode NULL = new ByteSlice(null);
    public final static ByteArrayNode EMPTY = new ByteSlice(new byte[0]);

    private final byte[] value;

    private ByteSlice(byte[] value) {
        this.value = value;
    }

    public static ByteArrayNode valueOf(byte[] bytes) {
        if (bytes == null)
            return NULL;
        if (bytes.length == 0)
            return EMPTY;
        return new ByteSlice(bytes);
    }

    @Override
    public DataType getDataType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
