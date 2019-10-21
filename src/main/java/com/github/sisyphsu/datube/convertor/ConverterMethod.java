package com.github.sisyphsu.datube.convertor;

import com.github.sisyphsu.datube.reflect.XType;
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

    /**
     * The distance in map, which used for shortest calculation.
     *
     * @return Distance
     */
    public abstract int getDistance();

    /**
     * This method is extensible or not.
     *
     * @return extensible
     */
    public abstract boolean isExtensible();

}
