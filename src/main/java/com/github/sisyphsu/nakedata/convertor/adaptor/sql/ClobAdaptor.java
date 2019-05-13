package com.github.sisyphsu.nakedata.convertor.adaptor.sql;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.sql.Clob;

/**
 * @author sulin
 * @since 2019-05-13 18:10:38
 */
public class ClobAdaptor extends Adaptor<Clob, String> {

    @Override
    protected String toTargetNotNull(Clob clob) {
        return null;
    }

    @Override
    protected Clob toSourceNotNull(String s) {
        return null;
    }

}
