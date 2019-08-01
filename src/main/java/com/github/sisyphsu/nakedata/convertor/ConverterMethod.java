package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.reflect.XType;
import lombok.Getter;

/**
 * ConverterMethod represent a method for converting data from srcClass to tgtClass
 *
 * @author sulin
 * @since 2019-08-01 20:41:42
 */
@Getter
public abstract class ConverterMethod {

    private final Class<?> srcClass;
    private final Class<?> tgtClass;

    public ConverterMethod(Class<?> srcClass, Class<?> tgtClass) {
        this.srcClass = srcClass;
        this.tgtClass = tgtClass;
    }

    /**
     * Convert the data to target instance
     *
     * @param data    Source data
     * @param tgtType Target Type
     * @return target instance
     */
    public abstract Object convert(Object data, XType tgtType);

    public abstract int getDistance();

    public abstract boolean isExtensible();

}
