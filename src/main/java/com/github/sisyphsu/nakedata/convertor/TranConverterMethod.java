package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.reflect.XType;

/**
 * Simple convert data from subclass to class
 *
 * @author sulin
 * @since 2019-08-01 20:13:39
 */
public class TranConverterMethod extends ConverterMethod {

    public TranConverterMethod(Class<?> srcClass, Class<?> tgtClass) {
        super(srcClass, tgtClass);
    }

    @Override
    public Object convert(Object data, XType tgtType) {
        return data;
    }

    @Override
    public int getDistance() {
        return 10;
    }

    @Override
    public boolean isExtensible() {
        return false;
    }

}
