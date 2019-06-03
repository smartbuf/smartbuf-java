package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * Scan Codec from current package, should find all official Codec implementation.
 *
 * @author sulin
 * @since 2019-06-03 20:18:54
 */
@Slf4j
public class CodecScanner {

    /**
     * Scan all Codec classes from current package.
     *
     * @return All Codec classes
     */
    @SuppressWarnings("unchecked")
    public static Set<Class<? extends Codec>> scanAllCodecs() {
        Class<?>[] classes = scanAllClasses(CodecScanner.class.getPackage().getName());
        Set<Class<? extends Codec>> result = new HashSet<>();
        for (Class<?> clz : classes) {
            if (clz == Codec.class || clz.isInterface() || Modifier.isAbstract(clz.getModifiers())) {
                continue;
            }
            if (Codec.class.isAssignableFrom(clz)) {
                result.add((Class<? extends Codec>) clz);
            }
        }
        return result;
    }

    /**
     * Scan all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */
    public static Class[] scanAllClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            throw new IllegalStateException("can't get classloader");
        }
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            throw new IllegalStateException("load resources failed for " + path, e);
        }
        List<Class> classes = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            classes.addAll(scanAllClasses(new File(resource.getFile()), packageName));
        }
        return classes.toArray(new Class[0]);
    }

    /**
     * Recursive method used to load all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class> scanAllClasses(File directory, String packageName) {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(scanAllClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(className));
                    log.debug("load class [{}]", className);
                } catch (ClassNotFoundException e) {
                    log.warn("load class [{}] failed.", className, e);
                }
            }
        }
        return classes;
    }

}
