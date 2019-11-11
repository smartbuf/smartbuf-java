package com.github.sisyphsu.smartbuf.converter;

import com.github.sisyphsu.smartbuf.reflect.BeanWriter;

/**
 * BeanInfo helps convert map or objectnode to T
 *
 * @author sulin
 * @since 2019-11-11 17:59:11
 */
public final class BeanInfo {

    private final BeanWriter writer;
    private final Object[]   values;

    public BeanInfo(BeanWriter writer, Object[] values) {
        this.writer = writer;
        this.values = values;
    }

    public BeanWriter getWriter() {
        return writer;
    }

    public Object[] getValues() {
        return values;
    }

}
