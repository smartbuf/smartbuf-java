package com.github.sisyphsu.nakedata.convertor.reflect;

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

    private GenBean<List<Long>> gen1;
    private GenBean<? extends List> gen2;
    private GenBean gen3;

    public static class GenBean<C extends Collection<Long>> {
        private C c;
    }

}
