package com.github.sisyphsu.canoe.reflect;

import com.github.sisyphsu.canoe.utils.ASMUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Build {@link Accessor} by class and its properties.
 *
 * @author sulin
 * @since 2019-10-29 15:42:49
 */
@SuppressWarnings("unchecked")
final class AccessorBuilder {

    static final Map<Class, Class<? extends Accessor>> ACCESSOR_CLS_MAP = new ConcurrentHashMap<>();

    private AccessorBuilder() {
    }

    /**
     * Build Accessor for the specified class and its properties
     *
     * @param cls        Bean class
     * @param properties Bean properties
     * @return Bean's accessor, it's reusable
     */
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

    /**
     * Build Accessor proxy for the specified class, it use asm to generate implementation.
     *
     * @param cls        Bean class
     * @param properties Bean properties
     * @return Bean's accessor proxy implementation
     */
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
            ASMUtils.addIntInstruction(mv, i);
            mv.visitVarInsn(Opcodes.ALOAD, 3); // t.
            if (prop.getter == null) {
                String fieldName = prop.field.getName();
                Class<?> fieldType = prop.field.getType();
                mv.visitFieldInsn(Opcodes.GETFIELD, clsName, fieldName, Type.getDescriptor(fieldType));
                if (fieldType.isPrimitive()) {
                    ASMUtils.addBoxInstruction(mv, fieldType);
                }
            } else {
                String methodName = prop.getter.getName();
                Class<?> retType = prop.getter.getReturnType();
                String retDescriptor = Type.getDescriptor(retType);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clsName, methodName, "()" + retDescriptor, false);
                if (retType.isPrimitive()) {
                    ASMUtils.addBoxInstruction(mv, retType);
                }
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

        return (Class<? extends Accessor>) ASMUtils.loadClass(cw, accessorClassName);
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
