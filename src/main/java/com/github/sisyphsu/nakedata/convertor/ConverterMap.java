package com.github.sisyphsu.nakedata.convertor;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Converter's map, used for searching path.
 *
 * @author sulin
 * @since 2019-08-01 21:45:34
 */
@Slf4j
public class ConverterMap {

    private Set<Class> classes = new HashSet<>();
    private Map<Class, Map<Class, ConverterMethod>> map = new HashMap<>();

    /**
     * Check the specified class, if it's new, flush CastConverter for it.
     */
    public void flushCastConverter(Class<?> cls) {
        if (classes.contains(cls)) {
            return;
        }
        synchronized (this) {
            for (Class<?> oldCls : classes) {
                if (oldCls.isAssignableFrom(cls)) {
                    this.putTran(cls, oldCls);
                } else if (cls.isAssignableFrom(oldCls)) {
                    this.putTran(oldCls, cls);
                }
            }
            classes.add(cls);
        }
    }

    public synchronized void putReal(ConverterMethod method) {
        this.flushCastConverter(method.getSrcClass());
        this.flushCastConverter(method.getTgtClass());
        map.computeIfAbsent(method.getSrcClass(), (c) -> new HashMap<>()).put(method.getTgtClass(), method);
    }

    public synchronized void putTran(Class srcClass, Class tgtClass) {
        ConverterMethod method = this.get(srcClass, tgtClass);
        if (method != null) {
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
        this.classes.clear();
    }

    public synchronized String printChart() {
        // prepare nodes
        Map<Class, String> nodeMap = new HashMap<>();
        for (Class clz : classes) {
            nodeMap.put(clz, "\"" + genNodeName(clz) + "\"");
        }
        // print lines
        StringBuilder buf = new StringBuilder();
        map.entrySet().stream().sorted((o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size())).forEach(entry -> {
            for (ConverterMethod method : entry.getValue().values()) {
                if (method.getTgtClass() == Object.class && method instanceof TranConverterMethod) {
                    continue;
                }
                String src = nodeMap.get(method.getSrcClass());
                String tgt = nodeMap.get(method.getTgtClass());
                // buf.append(src).append(" - ").append(method.getDistance()).append(" ->> ").append(tgt).append('\n');
                // buf.append(src).append(" -> ").append(tgt).append(':').append(method.getDistance()).append('\n');
                buf.append(src).append(" -> ").append(tgt).append(";\n");
            }
        });
//        for (Map<Class, ConverterMethod> value : map.values()) {
//            for (ConverterMethod method : value.values()) {
//                String src = nodeMap.get(method.getSrcClass());
//                String tgt = nodeMap.get(method.getTgtClass());
//                // buf.append(src).append(" - ").append(method.getDistance()).append(" ->> ").append(tgt).append('\n');
//                // buf.append(src).append(" -> ").append(tgt).append(':').append(method.getDistance()).append('\n');
//                buf.append(src).append(" -> ").append(tgt).append(";\n");
//            }
//        }
        return buf.toString();
    }

    private String genNodeName(Class cls) {
        if (cls.isArray()) {
            String itemName = genNodeName(cls.getComponentType());
            return itemName + "[]";
        }
        String[] parts = cls.getName().split("\\.");
        if (parts.length <= 0) {
            return "";
        }
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