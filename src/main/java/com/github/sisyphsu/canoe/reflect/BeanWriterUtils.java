package com.github.sisyphsu.canoe.reflect;

import com.github.sisyphsu.canoe.utils.ASMUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static com.github.sisyphsu.canoe.reflect.BeanHelper.findField;

/**
 * @author sulin
 * @since 2019-11-08 17:59:03
 */
public final class BeanWriterUtils {

    private static final Map<Class, BeanWriter> WRITER_MAP = new ConcurrentHashMap<>();

    private static final Pattern RE_SET = Pattern.compile("^set[A-Z].*$");

    private BeanWriterUtils() {
    }

    /**
     * Get an reusable {@link BeanHelper} instance of the specified class
     *
     * @param cls The specified class
     * @return cls's BeanHelper
     */
    public static BeanWriter build(Class<?> cls) {
        BeanWriter writer = WRITER_MAP.get(cls);
        if (writer == null) {
            writer = buildWriter(cls);
            WRITER_MAP.put(cls, writer);
        }
        return writer;
    }

    static BeanWriter buildWriter(Class<?> cls) {
        Map<String, BeanField> propMap = new TreeMap<>();
        // collect public field
        for (Field f : cls.getFields()) {
            String name = f.getName();
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod) || f.isAnnotationPresent(Deprecated.class)) {
                continue; // ignore non-public and transient and deprecated
            }
            propMap.put(name, new BeanField(name, f.getType()));
        }
        // collect valid getter
        for (Method method : cls.getMethods()) {
            String name = method.getName();
            int mod = method.getModifiers();
            if (Modifier.isAbstract(mod) || Modifier.isStatic(mod) || Modifier.isNative(mod)
                || method.isAnnotationPresent(Deprecated.class)
                || method.getParameterCount() != 1
                || method.getReturnType() != Void.class
                || !RE_SET.matcher(name).matches()) {
                continue;
            }
            name = name.substring(3); // void setXXX(XXX xxx)

            if (name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') {
                name = (char) (name.charAt(0) + 32) + name.substring(1);
            }
            BeanField bf = propMap.get(name);
            if (bf != null && bf.type != method.getParameterTypes()[0]) {
                continue; // ignore incompatible type
            }
            if (bf == null) {
                Field f = findField(cls, name, method.getParameterTypes()[0]);
                if (f != null && (Modifier.isTransient(f.getModifiers()) || f.isAnnotationPresent(Deprecated.class))) {
                    continue; // don't need transient or deprecated field
                }
                bf = new BeanField(name, method.getParameterTypes()[0]);
            }
            bf.getter = method;
            propMap.put(name, bf);
        }
        BeanField[] fields = propMap.values().toArray(new BeanField[0]);
        Class<? extends BeanWriter> writerCls = buildWriterClass(cls, fields);
        try {
            BeanWriter writer = writerCls.newInstance();
//            writer.init(fields);
            return writer;
        } catch (Exception e) {
            throw new IllegalArgumentException("build writer for " + cls + " failed.", e);
        }
    }

    @SuppressWarnings("unchecked")
    static Class<? extends BeanWriter> buildWriterClass(Class<?> cls, BeanField[] fields) {
        String clsName = cls.getName().replace('.', '/');
        String writerClsName = cls.getName() + "$$$Writer";

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, writerClsName.replace('.', '/'), null, BeanWriter.NAME, null);

        // public T$$$Writer()
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, BeanWriter.NAME, "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // public void setAll(T t, Object[] values)
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "setAll", "(Ljava/lang/Object;[Ljava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitTypeInsn(Opcodes.CHECKCAST, clsName);
        mv.visitVarInsn(Opcodes.ASTORE, 3);
        for (int i = 0, len = fields.length; i < len; i++) {
            BeanField prop = fields[i];
            mv.visitVarInsn(Opcodes.ALOAD, 3); // t
            mv.visitVarInsn(Opcodes.ALOAD, 2); // values
            ASMUtils.addIntInstruction(mv, i);
            mv.visitInsn(Opcodes.AALOAD);
            if (prop.setter == null) {
                Class<?> fieldType = prop.field.getType();
                String fieldName = prop.field.getName();
                String fieldDestriptor = Type.getDescriptor(fieldType);
                mv.visitTypeInsn(Opcodes.CHECKCAST, toNonPrimitiveClass(fieldType).getName().replace('.', '/'));
                if (fieldType.isPrimitive()) {
                    ASMUtils.addUnboxInstruction(mv, fieldType);
                }
                mv.visitFieldInsn(Opcodes.PUTFIELD, clsName, fieldName, fieldDestriptor);
            } else {
                Class<?> paramType = prop.setter.getParameterTypes()[0];
                String methodName = prop.setter.getName();
                String paramDestriptor = Type.getDescriptor(paramType);
                mv.visitTypeInsn(Opcodes.CHECKCAST, toNonPrimitiveClass(paramType).getName().replace('.', '/'));
                if (paramType.isPrimitive()) {
                    ASMUtils.addUnboxInstruction(mv, paramType);
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clsName, methodName, "(" + paramDestriptor + ")V", false);
            }
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();

        return (Class<? extends BeanWriter>) ASMUtils.loadClass(cw, writerClsName);
    }

    /**
     * Defines getAll for accessor
     */
    static Class toNonPrimitiveClass(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                type = Boolean.class;
            } else if (type == byte.class) {
                type = Byte.class;
            } else if (type == short.class) {
                type = Short.class;
            } else if (type == int.class) {
                type = Integer.class;
            } else if (type == long.class) {
                type = Long.class;
            } else if (type == float.class) {
                type = Float.class;
            } else if (type == double.class) {
                type = Double.class;
            } else {
                type = Character.class;
            }
        }
        return type;
    }

}
