package com.github.smartbuf.reflect;

import com.github.smartbuf.utils.ASMUtils;
import com.github.smartbuf.utils.ReflectUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanReaderBuilder helps build {@link BeanReader} for normal pojos
 *
 * @author sulin
 * @since 2019-11-08 18:02:42
 */
@SuppressWarnings("unchecked")
public final class BeanReaderBuilder {

    static final Map<Class, BeanReader> READER_MAP = new ConcurrentHashMap<>();

    private BeanReaderBuilder() {
    }

    /**
     * Get an reusable {@link BeanReader} instance of the specified class
     *
     * @param cls The specified class
     * @return cls's BeanHelper
     */
    public static BeanReader build(Class<?> cls) {
        BeanReader reader = READER_MAP.get(cls);
        if (reader == null) {
            reader = buildReader(cls);
            READER_MAP.put(cls, reader);
        }
        return reader;
    }

    /**
     * Parse the specified class's readable fields, then generate {@link BeanReader} proxy for them.
     */
    static BeanReader buildReader(Class<?> cls) {
        Map<String, BeanField> fieldMap = new TreeMap<>();
        // collect all fields
        ReflectUtils.findAllValidFields(cls).forEach(field -> {
            BeanField bf = new BeanField(field.getName(), field.getType());
            bf.field = field;
            bf.getter = ReflectUtils.findGetter(cls, field);
            if (Modifier.isPublic(field.getModifiers()) || bf.getter != null) {
                fieldMap.put(field.getName(), bf);
            }
        });
        // build BeanReader
        try {
            BeanField[] fields = fieldMap.values().toArray(new BeanField[0]);
            BeanReader.API api = buildReaderClass(cls, fields).getConstructor().newInstance();
            return new BeanReader(api, fields);
        } catch (Throwable e) {
            throw new IllegalArgumentException("build reader for " + cls + " failed.", e);
        }
    }

    /**
     * Build reader class for the specified class with fields
     */
    static Class<? extends BeanReader.API> buildReaderClass(Class<?> cls, BeanField[] fields) {
        String clsName = cls.getName().replace('.', '/');
        String readerClsName = cls.getName() + "$$$Reader";

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, readerClsName.replace('.', '/'), null, "java/lang/Object", new String[]{BeanReader.API_NAME});

        // public T$$$Reader()
        ASMUtils.addConstructor(cw);

        // public void getAll(Object o, Object[] values)
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getAll", "(Ljava/lang/Object;[Ljava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitTypeInsn(Opcodes.CHECKCAST, clsName);
        mv.visitVarInsn(Opcodes.ASTORE, 3);
        for (int i = 0, len = fields.length; i < len; i++) {
            BeanField prop = fields[i];
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

        cw.visitEnd();

        return (Class<? extends BeanReader.API>) ASMUtils.loadClass(cw, readerClsName);
    }

}
