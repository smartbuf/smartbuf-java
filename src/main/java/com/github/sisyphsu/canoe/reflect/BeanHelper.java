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

    private final Class<T> tClass;
    public final  Accessor accessor;

    private final String[]    names;
    private final BeanField[] fields;
    private final int         fieldCount;

    // initialize BeanMapper, each class should has only one mapper
    private BeanHelper(Class<T> cls) {
        this.tClass = cls;
        this.fields = findProperties(cls);
        this.fieldCount = fields.length;
        this.names = new String[fields.length];
        for (int i = 0, len = fields.length; i < len; i++) {
            names[i] = fields[i].name;
        }
        try {
            this.accessor = AccessorBuilder.buildAccessor(cls, fields);
        } catch (Exception e) {
            throw new IllegalArgumentException("build bean accessor failed: " + cls, e);
        }
    }

    public static <T> BeanHelper<T> valueOf(Class<T> cls) {
        BeanHelper mapper = MAPPERS.get(cls);
        if (mapper == null) {
            mapper = new BeanHelper(cls);
            MAPPERS.put(cls, mapper);
        }
        return mapper;
    }

    public String[] getNames() {
        return names;
    }

    public Object[] getValues(T t) {
        Object[] result = new Object[fieldCount];
        accessor.getAll(t, result);
        return result;
    }

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
            propMap.computeIfAbsent(name, BeanField::new).field = field;
        }
        // collect getter and setter field
        for (Method method : cls.getMethods()) {
            String name = method.getName();
            int mod = method.getModifiers();
            if (Modifier.isAbstract(mod) || Modifier.isStatic(mod) || Modifier.isNative(mod)) {
                continue;
            }
            boolean getter = true;
            if (RE_IS.matcher(name).matches() && method.getReturnType() == boolean.class) {
                name = name.substring(2);
            } else if (RE_GET.matcher(name).matches()) {
                name = name.substring(3);
            } else if (RE_SET.matcher(name).matches()) {
                name = name.substring(3);
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
                try {
                    field = cls.getDeclaredField(name);
                } catch (NoSuchFieldException ignored) {
                }
            }
            if (field != null && Modifier.isTransient(field.getModifiers())) {
                continue; // don't need transient field
            }
            properties.add(prop);
        }
        return properties.toArray(new BeanField[0]);
    }

}
