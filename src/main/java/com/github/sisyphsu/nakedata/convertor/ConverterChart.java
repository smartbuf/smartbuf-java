package com.github.sisyphsu.nakedata.convertor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Converter's chart, used for searching path.
 *
 * @author sulin
 * @since 2019-08-01 21:45:34
 */
public class ConverterChart {

    private Map<Class, Map<Class, ConverterMethod>> map = new HashMap<>();

    public synchronized void putReal(ConverterMethod method) {
        map.computeIfAbsent(method.getSrcClass(), (c) -> new HashMap<>()).put(method.getTgtClass(), method);
    }

    public synchronized void putTran(Class srcClass, Class tgtClass) {
        ConverterMethod method = this.get(srcClass, tgtClass);
        if (method == null) {
            return;
        }
        method = new TranConverterMethod(srcClass, tgtClass);
        map.computeIfAbsent(srcClass, (c) -> new HashMap<>()).put(tgtClass, method);
    }

    public synchronized Collection<ConverterMethod> get(Class srcType) {
        Map<Class, ConverterMethod> tgtMap = map.get(srcType);
        if (tgtMap == null) {
            return null;
        }
        return tgtMap.values();
    }

    public synchronized ConverterMethod get(Class srcType, Class tgtType) {
        Map<Class, ConverterMethod> tgtMap = map.get(srcType);
        if (tgtMap == null) {
            return null;
        }
        return tgtMap.get(tgtType);
    }

    public synchronized void clear() {
        this.map.clear();
    }
}