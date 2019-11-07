package com.github.sisyphsu.canoe.converter;

import com.github.sisyphsu.canoe.reflect.XType;
import com.github.sisyphsu.canoe.utils.ASMUtils;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ConverterPipeline represent an integrated converter pipeline,
 * it holds the full path from srcClass to tgtClass, like:
 * 1. string -> map -> POJO
 * 2. ObjectNode -> map -> POJO
 * 3. ByteNode -> byte[] -> Date
 *
 * @author sulin
 * @since 2019-05-20 15:24:12
 */
@Slf4j
public final class ConverterPipeline {

    public static boolean ENABLE_ASM = true;

    private static final AtomicInteger IDER = new AtomicInteger(0);

    private final Pipeline              pipeline;
    private final List<ConverterMethod> methods;

    public ConverterPipeline(List<ConverterMethod> methods) {
        List<RealConverterMethod> realConverterMethods = new ArrayList<>();
        for (ConverterMethod method : methods) {
            if (method instanceof RealConverterMethod) {
                realConverterMethods.add((RealConverterMethod) method);
            }
        }
        Pipeline pipeline = null;
        try {
            if (ENABLE_ASM) {
                pipeline = build(realConverterMethods);
            }
        } catch (Exception e) {
            log.error("build codec's pipeline failed: ", e);
        }
        this.pipeline = pipeline;
        this.methods = methods;
    }

    /**
     * Convert the specified source data to tgtType instance.
     *
     * @param data    Source data
     * @param tgtType Target type, with generic info
     * @return Target, match tgtType
     */
    public Object convert(Object data, XType tgtType) {
        if (pipeline != null) {
            return pipeline.convert(data, tgtType);
        }
        Object result = data;
        for (ConverterMethod method : methods) {
            result = method.convert(result, tgtType);
        }
        return result;
    }

    public List<ConverterMethod> getMethods() {
        return methods;
    }

    /**
     * Pipeline defines the specification of converter for {@link ConverterPipeline#build}
     */
    public interface Pipeline {
        Object convert(Object data, XType tgtType);
    }

    /**
     * Build pipeline instance for the specified methods, it use asm to improve optimize the performace.
     *
     * @param methods This methods make up the final pipeline
     * @return The final pipeline instance
     * @throws Exception If build pipeline failed
     */
    public static Pipeline build(List<RealConverterMethod> methods) throws Exception {
        String pipelineCls = Pipeline.class.getName() + "$$$Impl$" + IDER.incrementAndGet();
        String pipelineName = pipelineCls.replace('.', '/');
        String interfaceName = Pipeline.class.getName().replace('.', '/');

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, pipelineName, null, "java/lang/Object", new String[]{interfaceName});

        // public Codec codec[1...n];
        for (int i = 0; i < methods.size(); i++) {
            RealConverterMethod method = methods.get(i);
            Codec codec = method.codec;
            cw.visitField(Opcodes.ACC_PUBLIC, "codec_" + i, Type.getDescriptor(codec.getClass()), null, null);
        }

        // public constructor;
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // Object convert(Object data, XType tgtType);
        String descriptor = String.format("(Ljava/lang/Object;%s)Ljava/lang/Object;", Type.getDescriptor(XType.class));
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "convert", descriptor, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        Class<?> dataCls = Object.class;
        for (int i = 0; i < methods.size(); i++) {
            final RealConverterMethod method = methods.get(i);
            final Class<?> argType = method.method.getParameterTypes()[0];
            final Class<?> retType = method.method.getReturnType();
            final int varOffset = i + 3;
            // store var into LocalVariableTable, cast if needed
            if (method.getSrcClass() != dataCls) {
                mv.visitTypeInsn(Opcodes.CHECKCAST, method.getSrcClass().getName().replace('.', '/'));
            }
            mv.visitVarInsn(Opcodes.ASTORE, varOffset);
            // check nullable: if (data == null) return null;
            if (!method.annotation.nullable()) {
                Label label = new Label();
                mv.visitVarInsn(Opcodes.ALOAD, varOffset);
                mv.visitJumpInsn(Opcodes.IFNONNULL, label);
                mv.visitInsn(Opcodes.ACONST_NULL);
                mv.visitInsn(Opcodes.ARETURN);
                mv.visitLabel(label);
            }
            // prepare invoke
            String codecName = method.codec.getClass().getName().replace('.', '/');
            String codecDescriptor = Type.getDescriptor(method.codec.getClass());
            String methodName = method.method.getName();
            String methodDescriptor = Type.getMethodDescriptor(method.method);
            // this.codec_i.convert(data, ?)
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, pipelineName, "codec_" + i, codecDescriptor);
            mv.visitVarInsn(Opcodes.ALOAD, varOffset);
            if (argType.isPrimitive()) {
                ASMUtils.addUnboxInstruction(mv, argType);
            }
            if (method.hasTypeArg) {
                mv.visitVarInsn(Opcodes.ALOAD, 2); // optional parameter [tgtType]
            }
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, codecName, methodName, methodDescriptor, false);
            if (retType.isPrimitive()) {
                ASMUtils.addBoxInstruction(mv, retType);
            }
            dataCls = method.getTgtClass();
        }
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();

        Pipeline pipeline = (Pipeline) ASMUtils.loadClass(cw, pipelineCls).newInstance();
        for (int i = 0; i < methods.size(); i++) {
            RealConverterMethod method = methods.get(i);
            pipeline.getClass().getField("codec_" + i).set(pipeline, method.codec);
        }

        return pipeline;
    }

}
