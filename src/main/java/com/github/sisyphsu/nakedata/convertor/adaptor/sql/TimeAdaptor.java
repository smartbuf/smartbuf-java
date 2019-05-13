package com.github.sisyphsu.nakedata.convertor.adaptor.sql;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.math.BigInteger;
import java.sql.Time;

/**
 * @author sulin
 * @since 2019-05-13 18:06:10
 */
public class TimeAdaptor extends Adaptor<Time, BigInteger> {
    @Override
    protected BigInteger toTargetNotNull(Time time) {
        return null;
    }

    @Override
    protected Time toSourceNotNull(BigInteger bigInteger) {
        return null;
    }
}
