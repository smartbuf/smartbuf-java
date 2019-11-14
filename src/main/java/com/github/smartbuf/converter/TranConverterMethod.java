package com.github.smartbuf.converter;

import com.github.smartbuf.reflect.XType;

/**
 * Simple convert data from subclass to class
 *
 * @author sulin
 * @since 2019-08-01 20:13:39
 */
public final class TranConverterMethod extends ConverterMethod {

    public TranConverterMethod(Class<?> srcClass, Class<?> tgtClass) {
        super(srcClass, tgtClass);
    }

    @Override
    public Object convert(Object data, XType tgtType) {
        return data;
    }

    @Override
    public int getDistance() {
        return 1 << 4;
    }

    @Override
    public boolean isExtensible() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("[%s->%s]", getSrcClass().getName(), getTgtClass().getName());
    }
}
