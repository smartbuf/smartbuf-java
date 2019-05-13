package com.github.sisyphsu.nakedata.convertor.adaptor.sql;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * @author sulin
 * @since 2019-05-13 18:07:34
 */
public class TimestampAdaptor extends Adaptor<Timestamp, BigInteger> {

    @Override
    protected BigInteger toTargetNotNull(Timestamp timestamp) {
        return null;
    }

    @Override
    protected Timestamp toSourceNotNull(BigInteger bigInteger) {
        return null;
    }

}
