package com.github.sisyphsu.smartbuf.utils;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author sulin
 * @since 2019-11-06 20:16:10
 */
public class ASMUtilsTest {

    @Test
    public void test() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "Test", null, "java/lang/Object", null);

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

        ASMUtils.addIntInstruction(mv, 1 << 20);

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        try {
            ASMUtils.addBoxInstruction(null, Void.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        try {
            ASMUtils.addUnboxInstruction(null, Void.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

}
