package com.github.smartbuf.reflect;

import com.github.smartbuf.utils.ASMUtils;
import com.github.smartbuf.utils.ReflectUtils;
import org.objectweb.asm.*;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanWriterBuilder helps build {@link BeanWriter} for normal pojos
 *
 * @author sulin
 * @since 2019-11-08 17:59:03
 */
@SuppressWarnings("unchecked")
public final class BeanWriterBuilder {

    private static final Map<Class, BeanWriter> WRITER_MAP = new ConcurrentHashMap<>();

    private BeanWriterBuilder() {
    }

    /**
     * Get an reusable {@link BeanWriter} instance of the specified class
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

    /**
     * Parse the specified class's writeable fields, then generate {@link BeanWriter} proxy for them.
     */
    static BeanWriter buildWriter(Class<?> cls) {
        Map<String, BeanField> fieldMap = new TreeMap<>();
        // collect all field
        ReflectUtils.findAllValidFields(cls).forEach(field -> {
            BeanField bf = new BeanField(field.getName(), field.getType());
            bf.field = field;
            bf.setter = ReflectUtils.findSetter(cls, field);
            if (Modifier.isPublic(field.getModifiers()) || bf.setter != null) {
                fieldMap.put(field.getName(), bf);
            }
        });
        // build BeanWriter
        try {
            BeanField[] fields = fieldMap.values().toArray(new BeanField[0]);
            BeanWriter.API api = buildWriterClass(cls, fields).getConstructor().newInstance();
            return new BeanWriter(api, fields);
        } catch (Throwable e) {
            throw new IllegalArgumentException("build writer for " + cls + " failed.", e);
        }
    }

    /**
     * Build writer class for the specified class with fields
     */
    static Class<? extends BeanWriter.API> buildWriterClass(Class<?> cls, BeanField[] fields) {
        String clsName = cls.getName().replace('.', '/');
        String writerClsName = cls.getName() + "$$$Writer";

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, writerClsName.replace('.', '/'), null, "java/lang/Object", new String[]{BeanWriter.API_NAME});

        // public T$$$Writer()
        ASMUtils.addConstructor(cw);

        // public void setAll(T t, Object[] values)
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "setAll", "(Ljava/lang/Object;[Ljava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitTypeInsn(Opcodes.CHECKCAST, clsName);
        mv.visitVarInsn(Opcodes.ASTORE, 3);
        for (int i = 0, len = fields.length; i < len; i++) {
            BeanField prop = fields[i];
            // if (values[i]==null) jump
            Label label = new Label();
            mv.visitVarInsn(Opcodes.ALOAD, 2); // values
            ASMUtils.addIntInstruction(mv, i);
            mv.visitInsn(Opcodes.AALOAD);
            mv.visitJumpInsn(Opcodes.IFNULL, label);
            // t.setXXX(values[i])
            mv.visitVarInsn(Opcodes.ALOAD, 3);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
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
            mv.visitLabel(label);
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();

        return (Class<? extends BeanWriter.API>) ASMUtils.loadClass(cw, writerClsName);
    }

    /**
     * Convert type to no-primary class if it is primary
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
