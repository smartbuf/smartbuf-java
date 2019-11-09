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
 * @since 2019-11-08 18:02:42
 */
@SuppressWarnings("unchecked")
public final class BeanReaderBuilder {

    static final Map<Class, BeanReader> READER_MAP = new ConcurrentHashMap<>();

    static final Pattern RE_IS  = Pattern.compile("^is[A-Z_$].*$");
    static final Pattern RE_GET = Pattern.compile("^get[A-Z_$].*$");

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
        ReflectUtils.findAllFields(cls).forEach(f -> {
            BeanField field = new BeanField(f.getName(), f.getType());
            field.field = f;
            fieldMap.put(f.getName(), field);
        });
        // collect valid getter
        for (Method m : cls.getMethods()) {
            String name = m.getName();
            Class<?> retType = m.getReturnType();
            if (Modifier.isStatic(m.getModifiers()) || m.isAnnotationPresent(Deprecated.class)
                || m.getParameterCount() > 0
                || retType == void.class
                || retType == Void.class) {
                continue;
            }
            if (RE_IS.matcher(name).matches() && retType == boolean.class) {
                name = name.substring(2); // boolean isXXX()
            } else if (RE_GET.matcher(name).matches()) {
                name = name.substring(3); // xxx getXXX()
            } else {
                continue; // ignore invalid getter
            }
            if (name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') {
                name = (char) (name.charAt(0) + 32) + name.substring(1);
            }
            BeanField field = fieldMap.get(name);
            if (field != null && field.type == retType) {
                field.getter = m;
            }
        }
        // clean unreadable fields
        for (String name : new ArrayList<>(fieldMap.keySet())) {
            BeanField bf = fieldMap.get(name);
            int mod = bf.field.getModifiers();
            if (Modifier.isPublic(mod) || bf.getter != null) {
                continue;
            }
            fieldMap.remove(name);
        }
        // build BeanReader
        try {
            BeanField[] fields = fieldMap.values().toArray(new BeanField[0]);
            BeanReader.API api = buildReaderClass(cls, fields).newInstance();
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
