package com.github.sisyphsu.datube.convertor;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Converter's map, used for searching path.
 *
 * @author sulin
 * @since 2019-08-01 21:45:34
 */
@Slf4j
public final class ConverterMap {

    private Set<Class>                              classes   = ConcurrentHashMap.newKeySet();
    private Map<Class, Map<Class, ConverterMethod>> methodMap = new HashMap<>();

    /**
     * Try to put new ConverterMethod into this Map, may not take effect
     *
     * @param method new ConverterMethod instance
     */
    public synchronized void put(ConverterMethod method) {
        this.flushCastConverter(method.getSrcClass());
        this.flushCastConverter(method.getTgtClass());
        Map<Class, ConverterMethod> tgtMap = methodMap.computeIfAbsent(method.getSrcClass(), (c) -> new HashMap<>());
        ConverterMethod oldMethod = tgtMap.get(method.getTgtClass());
        if (oldMethod == null) {
            tgtMap.put(method.getTgtClass(), method);
        } else if (oldMethod instanceof TranConverterMethod) {
            if (method instanceof TranConverterMethod) {
                log.debug("replace Tran by new one: {} -> {}", method.getSrcClass(), method.getTgtClass());
            } else if (method instanceof RealConverterMethod) {
                log.debug("replace Tran by Real: {} -> {}", method.getSrcClass(), method.getTgtClass());
            } else {
                throw new IllegalArgumentException("unknown ConverterMethod: " + method);
            }
            tgtMap.put(method.getTgtClass(), method);
        } else if (oldMethod instanceof RealConverterMethod) {
            if (method instanceof TranConverterMethod) {
                log.debug("ignore Tran because Real exists: {} -> {}", method.getSrcClass(), method.getTgtClass());
            } else if (method instanceof RealConverterMethod) {
                log.debug("replace Real by new one: {} -> {}", method.getSrcClass(), method.getTgtClass());
                tgtMap.put(method.getTgtClass(), method);
            } else {
                throw new IllegalArgumentException("unknown ConverterMethod: " + method);
            }
        } else {
            throw new IllegalArgumentException("unknown oldMethod: " + oldMethod);
        }
    }

    /**
     * Get all ConverterMethods from srcClass
     */
    @SuppressWarnings("unchecked")
    public synchronized Collection<ConverterMethod> get(Class srcClass) {
        Map<Class, ConverterMethod> tgtMap = methodMap.get(srcClass);
        if (tgtMap == null) {
            return Collections.EMPTY_LIST;
        }
        return tgtMap.values();
    }

    /**
     * Get the ConverterMethod from srcClass to tgtClass
     */
    public synchronized ConverterMethod get(Class srcClass, Class tgtClass) {
        Map<Class, ConverterMethod> tgtMap = methodMap.get(srcClass);
        if (tgtMap == null) {
            return null;
        }
        return tgtMap.get(tgtClass);
    }

    /**
     * Check the specified class, if it's new, flush CastConverter for it.
     */
    public void flushCastConverter(Class<?> cls) {
        if (!classes.add(cls)) {
            return;
        }
        for (Class<?> oldCls : classes) {
            if (oldCls == cls) {
                continue;
            }
            Class srcClass = null, tgtClass = null;
            if (oldCls.isAssignableFrom(cls)) {
                srcClass = cls;
                tgtClass = oldCls;
            } else if (cls.isAssignableFrom(oldCls)) {
                srcClass = oldCls;
                tgtClass = cls;
            }
            if (srcClass != null) {
                this.put(new TranConverterMethod(srcClass, tgtClass));
            }
        }
    }

    /**
     * Print this ConverterMap to dot chart, which can used by OmniGraffle or diagrams.
     * https://github.com/francoislaberge/diagrams/#flowchart
     */
    public synchronized String printDot() {
        // prepare nodes
        Map<Class, String> nodeMap = new HashMap<>();
        for (Class clz : classes) {
            nodeMap.put(clz, "\"" + genNodeName(clz) + "\"");
        }
        // print lines
        StringBuilder buf = new StringBuilder();
        methodMap.entrySet().stream().sorted((o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size())).forEach(entry -> {
            for (ConverterMethod method : entry.getValue().values()) {
                if (method.getTgtClass() == Object.class && method instanceof TranConverterMethod) {
                    continue;
                }
                String src = nodeMap.get(method.getSrcClass());
                String tgt = nodeMap.get(method.getTgtClass());
                buf.append(src).append(" -> ").append(tgt).append(";\n");
            }
        });
        return buf.toString();
    }

    /**
     * Generate nodeName for the specified class, which should more readable and short.
     */
    private String genNodeName(Class cls) {
        if (cls.isArray()) {
            String itemName = genNodeName(cls.getComponentType());
            return itemName + "[]";
        }
        String[] parts = cls.getName().split("\\.");
        String nodeName = parts[parts.length - 1];
        if (parts.length > 1 && !parts[0].equals("java") && !parts[0].equals("javax")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length - 2; i++) {
                sb.append(parts[i].charAt(0)).append('.');
            }
            sb.append(parts[parts.length - 1]);
            nodeName = sb.toString();
        }
        return nodeName;
    }

}
