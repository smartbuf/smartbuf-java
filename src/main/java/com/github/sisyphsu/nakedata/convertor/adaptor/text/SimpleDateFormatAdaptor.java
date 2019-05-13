package com.github.sisyphsu.nakedata.convertor.adaptor.text;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.text.SimpleDateFormat;

/**
 * @author sulin
 * @since 2019-05-13 18:29:02
 */
public class SimpleDateFormatAdaptor extends Adaptor<SimpleDateFormat, String> {
    @Override
    protected String toTargetNotNull(SimpleDateFormat simpleDateFormat) {
        return null;
    }

    @Override
    protected SimpleDateFormat toSourceNotNull(String s) {
        return null;
    }
}
