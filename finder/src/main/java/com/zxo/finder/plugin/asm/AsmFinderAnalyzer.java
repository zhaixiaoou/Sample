package com.zxo.finder.plugin.asm;

import com.zxo.finder.plugin.AbsFinderAnalyzer;
import com.zxo.finder.plugin.FinderPlugin;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AsmFinderAnalyzer extends AbsFinderAnalyzer {

    private FinderClassVisitor classVisitor = new FinderClassVisitor();
    private FinderMethodVisitor methodVisitor = new FinderMethodVisitor();

    private String className;
    private String methodName;
    private String fieldName;
    private int lineNumber;

    @Override
    protected void onAnalyze(byte[] byteCodes, String absolutePath) {
        if (byteCodes == null){
            return;
        }
        ClassReader reader = new ClassReader(byteCodes);
        reader.accept(classVisitor, 0);
    }

    private class FinderClassVisitor extends ClassVisitor {

        public FinderClassVisitor(ClassWriter cw) {
            super(Opcodes.ASM6, cw);
        }

        public FinderClassVisitor(){
            super(Opcodes.ASM6);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            className = name;
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            methodName = name;
            return methodVisitor.setSource(super.visitMethod(access, name, descriptor, signature, exceptions));
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            fieldName = name;
            return super.visitField(access, name, descriptor, signature, value);
        }
    }

    private class FinderMethodVisitor extends MethodVisitor {

        public FinderMethodVisitor() {
            super(Opcodes.ASM6);
        }

        protected MethodVisitor setSource(MethodVisitor mv) {
            this.mv = mv;
            return this;
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            methodName = null;
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            lineNumber = line;
            super.visitLineNumber(line, start);
        }

        // 访问方法中的方法
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            String target = owner + "." + name + descriptor;
            if ((isClassMatched(owner) || isMethodMatched(target)) && !isIgnorePrefix(className+"."+methodName)){
                print(target, className, methodName, lineNumber, inputFile);
            } else {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }

        }

        // 访问方法中的局部变量
        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            String target = owner + "." + name + descriptor;

            if ((isClassMatched(owner) || isMethodMatched(target)) && !isIgnorePrefix(className+"."+methodName)){
                print(target, className, methodName, lineNumber, inputFile);
            } else {
                super.visitFieldInsn(opcode, owner, name, descriptor);
            }

        }
    }
}
