package com.zxo.finder.plugin.asm;


import com.zxo.finder.plugin.AbsFinderAnalyzer;
import com.zxo.finder.plugin.FinderPlugin;
import com.zxo.finder.plugin.IReplace;
import com.zxo.finder.plugin.TransformUtil;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

import static com.zxo.finder.plugin.TransformUtil.FIELD_DESCRIPTOR;
import static com.zxo.finder.plugin.TransformUtil.INTERNAL_NAME;
import static com.zxo.finder.plugin.TransformUtil.METHOD_DESCRIPTOR;
import static org.objectweb.asm.util.Printer.OPCODES;

public class AsmTraceAnalyzer extends AbsFinderAnalyzer {

    private AsmTraceFieldVisitor fieldVisitor = new AsmTraceFieldVisitor();
    private AsmTraceMethodVisitor methodVisitor = new AsmTraceMethodVisitor();
    private ClassVisitor traceVisitor ;
    @Override
    protected byte[] onAnalyze(byte[] byteCodes) {
        if (byteCodes == null){
            return null;
        }
        ClassReader reader = new ClassReader(byteCodes);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        traceVisitor = new TraceClassVisitor(new PrintWriter(System.out));
        traceVisitor = new AsmTraceClassVisitor(writer);
        reader.accept(traceVisitor, ClassReader.EXPAND_FRAMES);
        return  writer.toByteArray();
    }

    @Override
    protected HashMap<String, IReplace> getDefaultReplaceFieldTarget() {
        return null;
    }

    @Override
    protected HashMap<String, IReplace> getDefaultReplaceMethodTarget() {
        return null;
    }


    protected  void printLabel(String label){
        FinderPlugin.log(label);
    }


    private class AsmTraceAdviceAdapter extends AdviceAdapter{

        private String funName;

        Label startLabel = new Label();
        protected AsmTraceAdviceAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor);
            this.funName = name;
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            mv.visitLabel(startLabel);
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("enter" + funName);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            Label endLable = new Label();
            mv.visitTryCatchBlock(startLabel, endLable, endLable, null);
            mv.visitLabel(endLable);

            finallyBlock(Opcodes.ATHROW);
            // 将异常抛出
            mv.visitInsn(Opcodes.ATHROW);
            super.visitMaxs(maxStack, maxLocals);
        }

        private void finallyBlock(int opcode) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            if (opcode == Opcodes.ATHROW){
                mv.visitLdcInsn("err enter" + funName);
            } else {
                mv.visitLdcInsn("normal enter" + funName);
            }
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);
            if (opcode != ATHROW) {
                finallyBlock(opcode);
            }
        }
    }

    private  class AsmTraceClassVisitor extends ClassVisitor {

        public AsmTraceClassVisitor(ClassWriter cw) {
            super(Opcodes.ASM6, cw);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            printLabel("visit："+access+" "+ name +" " + signature + " extend " + superName);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            printLabel("visitMethod："+TransformUtil.transformAccess(access) +" "+name +descriptor);
            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
//            return methodVisitor.setSource();
            return new AsmTraceAdviceAdapter(Opcodes.ASM6, methodVisitor, access, name, descriptor);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            StringBuilder stringBuilder = new StringBuilder();
            if (value != null) {
                stringBuilder.append(" = ");
                if (value instanceof String) {
                    stringBuilder.append('\"').append(value).append('\"');
                } else {
                    stringBuilder.append(value);
                }
            }
            printLabel("visitField："+TransformUtil.transformAccess(access) +" "+ TransformUtil.appendDescriptor(FIELD_DESCRIPTOR, descriptor) +" "+name+stringBuilder.toString());
            return fieldVisitor.setSource(super.visitField(access, name, descriptor, signature, value));
        }

        @Override
        public void visitEnd() {
            printLabel("visitEnd");
            super.visitEnd();
        }
    }

    private class AsmTraceMethodVisitor extends MethodVisitor {
        public AsmTraceMethodVisitor(){
            super(Opcodes.ASM6);
        }

        protected MethodVisitor setSource(MethodVisitor methodVisitor){
            this.mv = methodVisitor;
            return this;
        }

        @Override
        public void visitParameter(String name, int access) {
            printLabel("visitParameter: "+TransformUtil.transformAccess(access) + " "+name);
            super.visitParameter(name, access);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            printLabel("MethodVisitor visitCode");
        }


        @Override
        public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
            printLabel("visitFrame:"+ TransformUtil.transformFrame(type, numLocal, local, numStack, stack));
            super.visitFrame(type, numLocal, local, numStack, stack);
        }

        @Override
        public void visitInsn(int opcode) {
            super.visitInsn(opcode);
            printLabel("visitInsn:"+OPCODES[opcode]);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            super.visitIntInsn(opcode, operand);
            printLabel("visitIntInsn:"+OPCODES[opcode]+" "+operand);
        }

        @Override
        public void visitLdcInsn(Object value) {
            super.visitLdcInsn(value);
            printLabel("visitIntInsn:"+"LDC "+" "+value);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            super.visitVarInsn(opcode, var);
            printLabel("visitIntInsn:"+OPCODES[opcode]+" "+var);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            super.visitJumpInsn(opcode, label);
            printLabel("visitJumpInsn:"+OPCODES[opcode]+" "+TransformUtil.appendLabel(label));
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            printLabel("visitMethodInsn："+ Printer.OPCODES[opcode]+" "+ TransformUtil.appendDescriptor(INTERNAL_NAME, owner)+"."+name+TransformUtil.appendDescriptor(METHOD_DESCRIPTOR, descriptor));
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            super.visitFieldInsn(opcode, owner, name, descriptor);
            printLabel("visitFieldInsn："+Printer.OPCODES[opcode] +" "+TransformUtil.appendDescriptor(INTERNAL_NAME, owner)+"."+name+":"+TransformUtil.appendDescriptor(FIELD_DESCRIPTOR, descriptor));
        }

        @Override
        public void visitLabel(Label label) {
            super.visitLabel(label);
            printLabel("visitLabel："+TransformUtil.appendLabel(label));
        }

        @Override
        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
            super.visitTryCatchBlock(start, end, handler, type);
        }

        @Override
        public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
            super.visitLocalVariable(name, descriptor, signature, start, end, index);
            printLabel("visitLocalVariable:"+"LOCALVARIABLE "+name+ " "+TransformUtil.appendDescriptor(FIELD_DESCRIPTOR, descriptor)+ " "+start+" "+end+" "+index);
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            super.visitLineNumber(line, start);
            printLabel("visitLineNumber："+"LINENUMBER "+line+" "+TransformUtil.appendLabel(start));
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack, maxLocals);
            printLabel("visitMaxs:"+"MAXSTACK = "+maxStack + " MAXLOCALS = "+ maxLocals);
        }


        @Override
        public void visitEnd() {
            super.visitEnd();
            printLabel("MethodVisitor visitEnd");
        }
    }

    private class AsmTraceFieldVisitor extends FieldVisitor{

        public AsmTraceFieldVisitor() {
            super(Opcodes.ASM6);
        }

        public FieldVisitor setSource(FieldVisitor fv){
            this.fv = fv;
            return this;
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            printLabel("visitFieldEnd：");
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            printLabel("visitAnnotation："+descriptor);
            return super.visitAnnotation(descriptor, visible);
        }

        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
            printLabel("visitTypeAnnotation："+descriptor);
            return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
        }
    }
}
