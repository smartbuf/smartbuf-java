package com.github.sisyphsu.canoe.reflect;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sulin
 * @since 2019-10-29 15:42:49
 */
@SuppressWarnings("unchecked")
final class AccessorBuilder {

    private static final Map<Class, Class<? extends Accessor>> ACCESSOR_CLS_MAP = new ConcurrentHashMap<>();

    private static final String BOOLEAN_NAME   = Boolean.class.getName().replace('.', '/');
    private static final String BYTE_NAME      = Byte.class.getName().replace('.', '/');
    private static final String SHORT_NAME     = Short.class.getName().replace('.', '/');
    private static final String INTEGER_NAME   = Integer.class.getName().replace('.', '/');
    private static final String LONG_NAME      = Long.class.getName().replace('.', '/');
    private static final String FLOAT_NAME     = Float.class.getName().replace('.', '/');
    private static final String DOUBLE_NAME    = Double.class.getName().replace('.', '/');
    private static final String CHARACTER_NAME = Character.class.getName().replace('.', '/');

    private static final String BOOLEAN_DESCRIPTOR   = Type.getDescriptor(Boolean.class);
    private static final String BYTE_DESCRIPTOR      = Type.getDescriptor(Byte.class);
    private static final String SHORT_DESCRIPTOR     = Type.getDescriptor(Short.class);
    private static final String INTEGER_DESCRIPTOR   = Type.getDescriptor(Integer.class);
    private static final String LONG_DESCRIPTOR      = Type.getDescriptor(Long.class);
    private static final String FLOAT_DESCRIPTOR     = Type.getDescriptor(Float.class);
    private static final String DOUBLE_DESCRIPTOR    = Type.getDescriptor(Double.class);
    private static final String CHARACTER_DESCRIPTOR = Type.getDescriptor(Character.class);

    public static Accessor buildAccessor(Class<?> cls, BeanField... properties) {
        Class<? extends Accessor> accessorCls = ACCESSOR_CLS_MAP.get(cls);
        if (accessorCls == null) {
            accessorCls = buildAccessorClass(cls, properties);
            ACCESSOR_CLS_MAP.put(cls, accessorCls);
        }
        try {
            return accessorCls.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("build accessor for " + cls + " failed.", e);
        }
    }

    public static Class<? extends Accessor> buildAccessorClass(Class<?> cls, BeanField... properties) {
        String clsName = cls.getName().replace('.', '/');
        String accessorClassName = cls.getName() + "$$$Accessor";
        String accessorName = accessorClassName.replace('.', '/');

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, accessorName, null, "java/lang/Object", new String[]{Accessor.NAME});

        // public T$$$Accessor()
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // public void getAll(Object o, Object[] values)
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getAll", "(Ljava/lang/Object;[Ljava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitTypeInsn(Opcodes.CHECKCAST, clsName);
        mv.visitVarInsn(Opcodes.ASTORE, 3);
        for (int i = 0, len = properties.length; i < len; i++) {
            BeanField prop = properties[i];
            mv.visitVarInsn(Opcodes.ALOAD, 2); // values
            mv.visitIntInsn(Opcodes.BIPUSH, i);
            mv.visitVarInsn(Opcodes.ALOAD, 3); // t.
            if (prop.getter == null) {
                String fieldName = prop.field.getName();
                String fieldDescriptor = Type.getDescriptor(prop.field.getType());
                mv.visitFieldInsn(Opcodes.GETFIELD, clsName, fieldName, fieldDescriptor);
                optionalBox(mv, fieldDescriptor);
            } else {
                String methodName = prop.getter.getName();
                String retDescriptor = org.objectweb.asm.Type.getDescriptor(prop.getter.getReturnType());
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clsName, methodName, "()" + retDescriptor, false);
                optionalBox(mv, retDescriptor);
            }
            mv.visitInsn(Opcodes.AASTORE);
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // public void setAll(T t, Object[] values)
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "setAll", "(Ljava/lang/Object;[Ljava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitTypeInsn(Opcodes.CHECKCAST, clsName);
        mv.visitVarInsn(Opcodes.ASTORE, 3);
        for (int i = 0, len = properties.length; i < len; i++) {
            BeanField prop = properties[i];
            mv.visitVarInsn(Opcodes.ALOAD, 3); // t
            mv.visitVarInsn(Opcodes.ALOAD, 2); // values
            mv.visitIntInsn(Opcodes.BIPUSH, i);
            mv.visitInsn(Opcodes.AALOAD);
            if (prop.setter == null) {
                Class<?> fieldType = prop.field.getType();
                String fieldName = prop.field.getName();
                String fieldDestriptor = Type.getDescriptor(fieldType);
                mv.visitTypeInsn(Opcodes.CHECKCAST, toNonPrimitiveClass(fieldType).getName().replace('.', '/'));
                optionalUnBox(mv, fieldDestriptor);
                mv.visitFieldInsn(Opcodes.PUTFIELD, clsName, fieldName, fieldDestriptor);
            } else {
                Class<?> paramType = prop.setter.getParameterTypes()[0];
                String methodName = prop.setter.getName();
                String paramDestriptor = Type.getDescriptor(paramType);
                mv.visitTypeInsn(Opcodes.CHECKCAST, toNonPrimitiveClass(paramType).getName().replace('.', '/'));
                optionalUnBox(mv, paramDestriptor);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clsName, methodName, "(" + paramDestriptor + ")V", false);
            }
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();

        return (Class<? extends Accessor>) ByteArrayClassLoader.loadClass(accessorClassName, cw.toByteArray());
    }

    static void optionalBox(MethodVisitor mv, String descriptor) {
        switch (descriptor) {
            case "Z":
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, BOOLEAN_NAME, "valueOf", "(Z)" + BOOLEAN_DESCRIPTOR, false);
                break;
            case "B":
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, BYTE_NAME, "valueOf", "(B)" + BYTE_DESCRIPTOR, false);
                break;
            case "S":
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, SHORT_NAME, "valueOf", "(S)" + SHORT_DESCRIPTOR, false);
                break;
            case "I":
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, INTEGER_NAME, "valueOf", "(I)" + INTEGER_DESCRIPTOR, false);
                break;
            case "J":
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, LONG_NAME, "valueOf", "(J)" + LONG_DESCRIPTOR, false);
                break;
            case "F":
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, FLOAT_NAME, "valueOf", "(F)" + FLOAT_DESCRIPTOR, false);
                break;
            case "D":
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, DOUBLE_NAME, "valueOf", "(D)" + DOUBLE_DESCRIPTOR, false);
                break;
            case "C":
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, CHARACTER_NAME, "valueOf", "(C)" + CHARACTER_DESCRIPTOR, false);
                break;
        }
    }

    static void optionalUnBox(MethodVisitor mv, String descriptor) {
        switch (descriptor) {
            case "Z":
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, BOOLEAN_NAME, "booleanValue", "()Z", false);
                break;
            case "B":
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, BYTE_NAME, "byteValue", "()B", false);
                break;
            case "S":
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SHORT_NAME, "shortValue", "()S", false);
                break;
            case "I":
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, INTEGER_NAME, "intValue", "()I", false);
                break;
            case "J":
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, LONG_NAME, "longValue", "()J", false);
                break;
            case "F":
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, FLOAT_NAME, "floatValue", "()F", false);
                break;
            case "D":
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DOUBLE_NAME, "doubleValue", "()D", false);
                break;
            case "C":
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, FLOAT_NAME, "charValue", "()C", false);
                break;
        }
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
            } else if (type == char.class) {
                type = Character.class;
            }
        }
        return type;
    }

    /**
     * Helps load byte[] as class
     */
    static final class ByteArrayClassLoader extends ClassLoader {

        static final ByteArrayClassLoader INSTANCE = new ByteArrayClassLoader();

        ByteArrayClassLoader() {
            super(ByteArrayClassLoader.class.getClassLoader());
        }

        static synchronized Class<?> loadClass(String name, byte[] code) {
            return INSTANCE.defineClass(name, code, 0, code.length);
        }
    }

}
