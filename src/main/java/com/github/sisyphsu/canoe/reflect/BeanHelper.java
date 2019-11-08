package com.github.sisyphsu.canoe.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * BeanHelper provides some useful features for replacing reflect operations,
 * includes getValues, setValues, etc.
 * <p>
 * BeanHelper doesn't treat pojo as map, but use array directly, it should has higher performance.
 *
 * @author sulin
 * @since 2019-10-29 18:03:17
 */
@SuppressWarnings("unchecked")
public final class BeanHelper<T> {

    private static final Map<Class, BeanHelper> MAPPERS = new ConcurrentHashMap<>();

    private static final Pattern RE_IS  = Pattern.compile("^is[A-Z].*$");
    private static final Pattern RE_GET = Pattern.compile("^get[A-Z].*$");
    private static final Pattern RE_SET = Pattern.compile("^set[A-Z].*$");

    public final Accessor accessor;

    private final BeanField[] fields;
    private final String[]    fieldNames;
    private final byte[]      fieldTypes;
    private final int         fieldCount;

    // initialize BeanMapper, each class should has only one mapper
    private BeanHelper(Class<T> cls) {
        this.fields = findProperties(cls);
        this.fieldCount = fields.length;
        this.fieldNames = new String[fields.length];
        this.fieldTypes = new byte[fields.length];
        for (int i = 0, len = fields.length; i < len; i++) {
            BeanField field = fields[i];

            fieldNames[i] = field.name;
        }
        this.accessor = AccessorBuilder.buildAccessor(cls, fields);
    }

    /**
     * Get an reusable {@link BeanHelper} instance of the specified class
     *
     * @param cls The specified class
     * @return cls's BeanHelper
     */
    public static <T> BeanHelper<T> valueOf(Class<T> cls) {
        BeanHelper mapper = MAPPERS.get(cls);
        if (mapper == null) {
            mapper = new BeanHelper(cls);
            MAPPERS.put(cls, mapper);
        }
        return mapper;
    }

    /**
     * Get all fieldNames which has getter or setter.
     *
     * @return Field names
     */
    public String[] getFieldNames() {
        return fieldNames;
    }

    /**
     * Get all values of the specified object's properties.
     *
     * @param t The specified object to get values
     * @return t's all values
     */
    public Object[] getValues(T t) {
        Object[] result = new Object[fieldCount];
        accessor.getAll(t, result);
        return result;
    }

    /**
     * Set all values of the specified object' properties
     *
     * @param t    The specified object to set values
     * @param vals all values to setup
     */
    public void setValues(T t, Object[] vals) {
        if (vals == null || vals.length != fieldCount) {
            throw new IllegalArgumentException("invalid values");
        }
        accessor.setAll(t, vals);
    }

    /**
     * Find properties from the specified class
     *
     * @param cls The specified class
     * @return All properties which support mapper, like public field and field with getter/setter
     */
    public static BeanField[] findProperties(Class<?> cls) {
        Map<String, BeanField> propMap = new TreeMap<>();
        // collect public field
        for (Field field : cls.getFields()) {
            String name = field.getName();
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) {
                continue; // ignore non-public and transient
            }
            if (field.isAnnotationPresent(Deprecated.class)) {
                continue; // don't need deprecated field
            }
            propMap.computeIfAbsent(name, BeanField::new).field = field;
        }
        // collect getter and setter field
        for (Method method : cls.getMethods()) {
            String name = method.getName();
            int mod = method.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isNative(mod)) {
                continue;
            }
            if (method.isAnnotationPresent(Deprecated.class)) {
                continue; // don't need deprecated method
            }
            boolean getter = true;
            if (RE_IS.matcher(name).matches() && method.getReturnType() == boolean.class) {
                name = name.substring(2); // boolean isXXX()
            } else if (RE_GET.matcher(name).matches() && method.getReturnType() != Void.class && method.getParameterCount() == 0) {
                name = name.substring(3); // xxx getXXX()
            } else if (RE_SET.matcher(name).matches() && method.getReturnType() == Void.class && method.getParameterCount() == 1) {
                name = name.substring(3); // void setXXX(XXX xxx)
                getter = false;
            } else {
                continue; // ignore invalid method
            }
            name = (char) (name.charAt(0) + 32) + name.substring(1);
            BeanField prop = propMap.computeIfAbsent(name, BeanField::new);
            if (getter) {
                prop.getter = method;
            } else {
                prop.setter = method;
            }
        }
        // clean properties
        List<BeanField> properties = new ArrayList<>();
        for (String name : new ArrayList<>(propMap.keySet())) {
            BeanField prop = propMap.get(name);
            Field field = prop.field;
            if (prop.field == null) {
                field = findField(cls, name);
            }
            if (field != null) {
                if (Modifier.isTransient(field.getModifiers())) {
                    continue; // don't need transient field
                }
                if (field.isAnnotationPresent(Deprecated.class)) {
                    continue; // don't need deprecated field
                }
            } else if (prop.getter == null || prop.setter == null) {
                continue; // field must has getter and setter
            }
            properties.add(prop);
        }
        return properties.toArray(new BeanField[0]);
    }

    // find the specified field from cls, includes superclass
    static Field findField(Class cls, String name) {
        if (cls == Object.class) {
            return null;
        }
        try {
            return cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return findField(cls.getSuperclass(), name);
        }
    }

    // find the specified field from cls, includes superclass
    static Field findField(Class cls, String name, Class type) {
        if (cls == Object.class) {
            return null;
        }
        Field field = null;
        try {
            field = cls.getDeclaredField(name);
        } catch (NoSuchFieldException ignored) {
        }
        if (field != null && field.getType() == type) {
            return field;
        } else {
            return findField(cls.getSuperclass(), name, type);
        }
    }
}
