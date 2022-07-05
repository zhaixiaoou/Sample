package com.zxo.finder.plugin.asm;

import com.zxo.finder.plugin.AbsFinderAnalyzer;
import com.zxo.finder.plugin.FinderPlugin;
import com.zxo.finder.plugin.IReplace;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class AsmFinderAnalyzer extends AbsFinderAnalyzer {

    private final String REPLACE_OWNER = "com/zxo/sample/BaseInfo";
    private final String REPLACE_DESCRIPTOR_STRING = "()Ljava/lang/String;";
    private final String REPLACE_DESCRIPTOR_INT = "()I";

    private FinderClassVisitor classVisitor;
    private FinderMethodVisitor methodVisitor = new FinderMethodVisitor();
    private FinderClassWriter classWriter;

    private String className;
    private String methodName;
    private String fieldName;
    private int lineNumber;

    @Override
    protected byte[] onAnalyze(byte[] byteCodes) {
        if (byteCodes == null) {
            return null;
        }
        ClassReader reader = new ClassReader(byteCodes);
        classWriter = new FinderClassWriter(reader, ClassWriter.COMPUTE_FRAMES, classLoader);
        classVisitor = new FinderClassVisitor(classWriter);
        reader.accept(classVisitor, 0);

        return classWriter.toByteArray();
    }

    @Override
    protected HashMap<String, IReplace> getDefaultReplaceFieldTarget() {
        HashMap<String, IReplace> fieldTarget = new HashMap<>();
        fieldTarget.put("android/os/Build.BRAND:Ljava/lang/String;", new ASMReplaceBean(REPLACE_OWNER, "getBrand", REPLACE_DESCRIPTOR_STRING));
        fieldTarget.put("android/os/Build$VERSION.RELEASE:Ljava/lang/String;", new ASMReplaceBean(REPLACE_OWNER, "getAndroidVersion", REPLACE_DESCRIPTOR_STRING));
        return fieldTarget;
    }

    @Override
    protected HashMap<String, IReplace> getDefaultReplaceMethodTarget() {
        HashMap<String, IReplace> methodTarget = new HashMap<>();
        methodTarget.put("android/telephony/TelephonyManager.getNetworkType()I", new ASMReplaceBean(REPLACE_OWNER, "getNetworkType", REPLACE_DESCRIPTOR_INT));
        return methodTarget;
    }


    private class FinderClassVisitor extends ClassVisitor {

        public FinderClassVisitor(ClassWriter cw) {
            super(Opcodes.ASM6, cw);
        }

        public FinderClassVisitor() {
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

        // 倒数第一
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
            if ((isClassMatched(owner) || isMethodMatched(target)) && !isIgnorePrefix(className + "." + methodName)) {
                printLabel("调用方法名");
                print(target, className, methodName, lineNumber, inputFile);
            }
            if (isReplaceEnable() && !isReplaceIgnore(className + "." + methodName)) {
                IReplace replaceTarget = getReplaceMethodTarget(target);
                if (replaceTarget instanceof ASMReplaceBean) {
                    ASMReplaceBean replaceBean = (ASMReplaceBean) replaceTarget;
                    String modifyTarget = replaceBean.owner + "." + replaceBean.name + descriptor;
                    printReplace(target, modifyTarget, className, methodName, lineNumber, inputFile);
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, replaceBean.owner, replaceBean.name, replaceBean.descriptor, false);
                    return;
                }
            }

            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }

        // 访问方法中的局部变量
        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            String target = owner + "." + name + ":" + descriptor;

            // 检查扫描
            if ((isClassMatched(owner) || isFieldMatched(target)) && !isIgnorePrefix(className + "." + methodName)) {
                printLabel("调用属性值");
                print(target, className, methodName, lineNumber, inputFile);
            }
            try {
                if (isReplaceEnable() && !isReplaceIgnore(className + "." + methodName)) {
                    IReplace replaceTarget = getReplaceFieldTarget(target);
                    if (replaceTarget instanceof ASMReplaceBean) {
                        ASMReplaceBean replaceBean = (ASMReplaceBean) replaceTarget;
                        String modifyTarget = replaceBean.owner + "." + replaceBean.name + ":" + descriptor;
                        printReplace(target, modifyTarget, className, methodName, lineNumber, inputFile);
                        super.visitMethodInsn(Opcodes.INVOKESTATIC, replaceBean.owner, replaceBean.name, replaceBean.descriptor, false);
                        return;
                    }
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }

            super.visitFieldInsn(opcode, owner, name, descriptor);


        }

        // 1
        @Override
        public void visitCode() {
            super.visitCode();
        }

        // 2
        @Override
        public void visitInsn(int opcode) {
            super.visitInsn(opcode);
        }

        // 倒数第二
        // 如果新增变量，需要更新操作数栈 即更新slots
        @Override
        public void visitMaxs(int maxStack, int maxLocals) {

            super.visitMaxs(maxStack, maxLocals);
        }
    }

    private class FinderClassWriter extends ClassWriter {

        private ClassLoader classLoader;

        public FinderClassWriter(ClassReader classReader, int flags) {
            this(classReader, flags, null);
        }

        public FinderClassWriter(ClassReader classReader, int flags, ClassLoader classLoader) {
            super(classReader, flags);
            this.classLoader = classLoader;
        }

        @Override
        protected String getCommonSuperClass(String type1, String type2) {

            if (classLoader == null) {
                return super.getCommonSuperClass(type1, type2);
            }

            Class<?> c;
            Class<?> d;
            ClassLoader classLoader = this.classLoader;
            try {
                c = Class.forName(type1.replace('/', '.'), false, classLoader);
                d = Class.forName(type2.replace('/', '.'), false, classLoader);
            } catch (Exception e) {
                throw new RuntimeException(
                        String.format(
                                "Unable to find common supper type for %s and %s.", type1, type2),
                        e);
            }
            if (c.isAssignableFrom(d)) {
                return type1;
            }
            if (d.isAssignableFrom(c)) {
                return type2;
            }
            if (c.isInterface() || d.isInterface()) {
                return "java/lang/Object";
            } else {
                do {
                    c = c.getSuperclass();
                } while (!c.isAssignableFrom(d));
                return c.getName().replace('.', '/');
            }
        }
    }
}
