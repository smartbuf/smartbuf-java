package com.github.smartbuf.reflect;

import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author sulin
 * @since 2019-07-16 12:04:04
 */
@Data
public class GenericModel<T extends Number, X, R extends CharSequence> {

    private T t;
    private X x;
    private R r;

    private Map<?, R> rMap;
    private List<? extends Date> dates;
    private AtomicReference<? super ArrayList<? super Integer>> ref;

    private GenBean<AbstractList<Long>> gen1;
    private GenBean<? extends AbstractList> gen2;
    private GenBean gen3;

    private GenPojo<List<String>> pojo;

    public static class GenBean<C extends List<Long>> {
        private C c;
    }

    public static class GenPojo<Z extends List> {
        private Z z;
    }

    public void test() {
        gen1.c.add(null);
        gen2.c.add("");
        gen3.c.add(null);
    }

}
