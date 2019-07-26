package com.github.sisyphsu.nakedata.convertor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Data converter
 *
 * @author sulin
 * @since 2019-07-11 14:24:09
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {METHOD})
public @interface Converter {

    /**
     * Indicate this converter accept null or not.
     *
     * @return default false, means null shouldn't be passed into converter.
     */
    boolean nullable() default false;

}
