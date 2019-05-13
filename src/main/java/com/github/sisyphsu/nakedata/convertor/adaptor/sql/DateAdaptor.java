package com.github.sisyphsu.nakedata.convertor.adaptor.sql;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.math.BigInteger;
import java.sql.Date;

/**
 * @author sulin
 * @since 2019-05-13 18:06:50
 */
public class DateAdaptor extends Adaptor<Date, BigInteger> {

    @Override
    protected BigInteger toTargetNotNull(Date date) {
        return null;
    }

    @Override
    protected Date toSourceNotNull(BigInteger bigInteger) {
        return null;
    }
    
}
