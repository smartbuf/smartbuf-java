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
 * @since 2019-11-08 18:02:42
 */
public final class BeanReaderUtils {

    private static final Map<Class, BeanReader> READERS = new ConcurrentHashMap<>();

    private static final Pattern RE_IS  = Pattern.compile("^is[A-Z_$].*$");
    private static final Pattern RE_GET = Pattern.compile("^get[A-Z_$].*$");

    private BeanReaderUtils() {
    }

    /**
     * Get an reusable {@link BeanHelper} instance of the specified class
     *
     * @param cls The specified class
     * @return cls's BeanHelper
     */
    public static BeanReader build(Class<?> cls) {
        BeanReader reader = READERS.get(cls);
        if (reader == null) {
            reader = buildReader(cls);
            READERS.put(cls, reader);
        }
        return reader;
    }

    static BeanReader buildReader(Class<?> cls) {
        Map<String, BeanField> propMap = new TreeMap<>();
        // collect public field
        for (Field f : cls.getFields()) {
            String name = f.getName();
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod) || f.isAnnotationPresent(Deprecated.class)) {
                continue; // ignore non-public and transient and deprecated
            }
            BeanField bf = new BeanField(name, f.getType());
            bf.field = f;
            propMap.put(name, bf);
        }
        // collect valid getter
        for (Method method : cls.getMethods()) {
            String name = method.getName();
            int mod = method.getModifiers();
            if (Modifier.isAbstract(mod) || Modifier.isStatic(mod) || Modifier.isNative(mod)
                || method.isAnnotationPresent(Deprecated.class)
                || method.getParameterCount() > 0
                || method.getReturnType() == Void.class) {
                continue;
            }
            if (RE_IS.matcher(name).matches() && method.getReturnType() == boolean.class) {
                name = name.substring(2); // boolean isXXX()
            } else if (RE_GET.matcher(name).matches()) {
                name = name.substring(3); // xxx getXXX()
            } else {
                continue; // ignore invalid getter
            }
            if (name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') {
                name = (char) (name.charAt(0) + 32) + name.substring(1);
            }
            BeanField bf = propMap.get(name);
            if (bf != null && bf.type != method.getReturnType()) {
                continue; // ignore incompatible type
            }
            if (bf == null) {
                Field f = findField(cls, name, method.getReturnType());
                if (f != null && (Modifier.isTransient(f.getModifiers()) || f.isAnnotationPresent(Deprecated.class))) {
                    continue; // don't need transient or deprecated field
                }
                bf = new BeanField(name, method.getReturnType());
            }
            bf.getter = method;
            propMap.put(name, bf);
        }
        BeanField[] fields = propMap.values().toArray(new BeanField[0]);
        Class<? extends BeanReader.API> readerCls = buildReaderClass(cls, fields);
        try {
            BeanReader.API api = readerCls.newInstance();
            return new BeanReader(api, fields);
        } catch (Exception e) {
            throw new IllegalArgumentException("build reader for " + cls + " failed.", e);
        }
    }

    @SuppressWarnings("unchecked")
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
