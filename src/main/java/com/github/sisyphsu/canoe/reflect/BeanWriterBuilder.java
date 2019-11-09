package com.github.sisyphsu.canoe.reflect;

import com.github.sisyphsu.canoe.utils.ASMUtils;
import com.github.sisyphsu.canoe.utils.ReflectUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author sulin
 * @since 2019-11-08 17:59:03
 */
@SuppressWarnings("unchecked")
public final class BeanWriterBuilder {

    private static final Map<Class, BeanWriter> WRITER_MAP = new ConcurrentHashMap<>();

    private static final Pattern RE_SET = Pattern.compile("^set[A-Z_$].*$");

    private BeanWriterBuilder() {
    }

    /**
     * Get an reusable  instance of the specified class
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
        Map<String, BeanField> fieldMap = new TreeMap<>();
        // collect all field
        ReflectUtils.findAllValidFields(cls).forEach(f -> {
            BeanField field = new BeanField(f.getName(), f.getType());
            field.field = f;
            fieldMap.put(f.getName(), field);
        });
        // collect valid setter
        for (Method m : cls.getMethods()) {
            String name = m.getName();
            if (Modifier.isStatic(m.getModifiers()) || m.isAnnotationPresent(Deprecated.class)
                || (m.getReturnType() != Void.class && m.getReturnType() != void.class)
                || m.getParameterCount() != 1
                || !RE_SET.matcher(name).matches()) {
                continue;
            }
            if (name.charAt(3) >= 'A' && name.charAt(3) <= 'Z') {
                name = (char) (name.charAt(3) + 32) + name.substring(4);
            } else {
                name = name.substring(3);
            }
            BeanField field = fieldMap.get(name);
            if (field != null && field.cls == m.getParameterTypes()[0]) {
                field.setter = m;
            }
        }
        // clean unwriteable fields
        for (String name : new ArrayList<>(fieldMap.keySet())) {
            BeanField bf = fieldMap.get(name);
            int mod = bf.field.getModifiers();
            if (Modifier.isPublic(mod) || bf.setter != null) {
                continue;
            }
            fieldMap.remove(name);
        }
        // build BeanWriter
        try {
            BeanField[] fields = fieldMap.values().toArray(new BeanField[0]);
            BeanWriter.API api = buildWriterClass(cls, fields).newInstance();
            return new BeanWriter(api, fields);
        } catch (Throwable e) {
            throw new IllegalArgumentException("build writer for " + cls + " failed.", e);
        }
    }

    static Class<? extends BeanWriter.API> buildWriterClass(Class<?> cls, BeanField[] fields) {
        String clsName = cls.getName().replace('.', '/');
        String writerClsName = cls.getName() + "$$$Writer";

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, writerClsName.replace('.', '/'), null, "java/lang/Object", new String[]{BeanWriter.API_NAME});

        // public T$$$Writer()
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
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

        return (Class<? extends BeanWriter.API>) ASMUtils.loadClass(cw, writerClsName);
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
