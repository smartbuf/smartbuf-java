package com.github.sisyphsu.nakedata.convertor;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Converter's map, used for searching path.
 *
 * @author sulin
 * @since 2019-08-01 21:45:34
 */
@Slf4j
public class ConverterMap {

    private static String CHART_SVG = System.getProperty("user.home") + "/ConverterMap.svg";

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
        this.classes.clear();
    }

    public synchronized void printChart() throws Exception {
        StringBuilder buf = new StringBuilder();
        // print nodes
        Map<Class, String> nodeMap = new HashMap<>();
        for (Class clz : classes) {
            String[] parts = clz.getName().split("\\.");
            if (parts.length <= 0) {
                continue;
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
            nodeMap.put(clz, '"' + nodeName + '"');
        }
        for (String s : nodeMap.values()) {
            if (buf.length() > 0) {
                buf.append(",\n");
            }
            buf.append(s);
        }
        buf.append(";\n");
        // print lines
        for (Map<Class, ConverterMethod> value : map.values()) {
            for (ConverterMethod method : value.values()) {
                String src = nodeMap.get(method.getSrcClass());
                String tgt = nodeMap.get(method.getTgtClass());
                buf.append(src).append("\t->").append(tgt).append("\t:").append(method.getDistance()).append(";\n");
            }
        }
        // write as smcat
        File file = File.createTempFile("converter-map", ".smcat");
        PrintWriter writer = new PrintWriter(file);
        writer.print(buf.toString());
        writer.close();
        log.info("generate smcat source: {}", file.toString());
        // try to generate svg
        try {
            ProcessBuilder pb = new ProcessBuilder("smcat", file.toString(), "-o", CHART_SVG).inheritIO();
            Process p = pb.start();
            p.waitFor(15, TimeUnit.SECONDS);
            p.destroyForcibly();
            log.info("generate smcat svg: {}", CHART_SVG);
        } catch (Exception e) {
            log.error("generate svg failed: ", e);
        }
    }

}