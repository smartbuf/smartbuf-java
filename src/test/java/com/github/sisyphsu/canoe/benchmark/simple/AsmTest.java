package com.github.sisyphsu.canoe.benchmark.simple;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author sulin
 * @since 2019-10-28 21:14:21
 */
public class AsmTest {

    private static class ByteArrayClassLoader extends ClassLoader {

        public ByteArrayClassLoader() {
            super(ByteArrayClassLoader.class.getClassLoader());
        }

        public synchronized Class<?> getClass(String name, byte[] code) {
            if (name == null) {
                throw new IllegalArgumentException("");
            }
            return defineClass(name, code, 0, code.length);
        }
    }

    @Test
    public void test5() throws Exception {
        Pojo pojo = new Pojo();
        pojo.id = 1;
        pojo.time = System.currentTimeMillis();
        pojo.name = "hello";

        Class<? extends BeanGetter> getterCls = createGetter(Pojo.class,
            Pojo.class.getDeclaredMethod("getId"),
            Pojo.class.getDeclaredMethod("getTime"),
            Pojo.class.getDeclaredMethod("getName"));

        BeanGetter getter = getterCls.getDeclaredConstructor(Pojo.class).newInstance(pojo);
        System.out.println(getter);

        Object[] result = getter.getAll();

        System.out.println(Arrays.toString(result));
    }

    public static Class<? extends BeanGetter> createGetter(Class cls, Method... methods) {
        String className = cls.getName() + "$Getter";
        String classPath = className.replace('.', '/');
        String beanClass = cls.getName().replace('.', '/');
        String clsDescriptor = Type.getDescriptor(cls);
        String getterIntefacer = BeanGetter.class.getName().replace('.', '/');

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, classPath, null, "java/lang/Object", new String[]{getterIntefacer});

        // public T delegate;
        cw.visitField(Opcodes.ACC_PUBLIC, "delegate", "L" + beanClass + ";", null, null).visitEnd();

        // public T$Getter(T){}
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(" + clsDescriptor + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitFieldInsn(Opcodes.PUTFIELD, classPath, "delegate", clsDescriptor);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // public Object[] getAll{ Object[] result = new Object[]; for() result[i]=getValue(); return result;}
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getAll", "()[Ljava/lang/Object;", null, null);
        mv.visitCode();
        // Object[] result = new Object[100]
        mv.visitIntInsn(Opcodes.BIPUSH, methods.length);
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        mv.visitVarInsn(Opcodes.ASTORE, 1);

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            String methodDescriptor = Type.getDescriptor(method.getReturnType());

            // result[i] = delegate.getXXX();
            mv.visitVarInsn(Opcodes.ALOAD, 1); // ???
            mv.visitIntInsn(Opcodes.BIPUSH, i);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, classPath, "delegate", clsDescriptor);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, beanClass, methodName, "()" + methodDescriptor, false);
            mv.visitInsn(Opcodes.AASTORE);
        }

        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(0, 0); // compute_maxs
        mv.visitEnd();

        cw.visitEnd();

        byte[] code = cw.toByteArray();

        return (Class<? extends BeanGetter>) new ByteArrayClassLoader().getClass(className, code);
    }

    public interface BeanGetter {
        Object[] getAll();
    }

    @Data
    public static class Pojo {
        private Integer id;
        private String  name;
        private Long    time;
    }

}
