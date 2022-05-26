package com.zxo.finder.plugin;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 分析字节码基础类
 */
public abstract class AbsFinderAnalyzer {

    abstract protected void onAnalyze(byte[] byteCodes, String absolutePath);
    abstract protected byte[] onAnalyzeJar(byte[] byteCodes, ClassLoader classLoader);

    private FinderConfig config;

    private final HashSet<String> targetClasses = new HashSet<>();
    private final HashSet<String> targetMethods = new HashSet<>();
    private final HashSet<String> targetFields = new HashSet<>();
    private final HashMap<String, IReplace> replaceFieldTarget = new HashMap<>();
    private final HashMap<String, IReplace> replaceMethodTarget = new HashMap<>();
    private final List<String> replaceIgnorePrefixes = new ArrayList<>();

    protected File inputFile;

    public void setConfig(FinderConfig config) {
        this.config = config;
        if (config == null) return;
        if (config.classes != null) {
            targetClasses.addAll(config.classes);
        }
        if (config.methods != null) {
            targetMethods.addAll(config.methods);
        }
        if (config.fields != null){
            targetFields.addAll(config.fields);
        }
        if (config.replaceEnable){
            replaceFieldTarget.putAll(getDefaultReplaceFieldTarget());
            replaceMethodTarget.putAll(getDefaultReplaceMethodTarget());
            if ( config.ignoreReplacePrefixes != null && !config.ignoreReplacePrefixes.isEmpty()){
                replaceIgnorePrefixes.addAll(config.ignoreReplacePrefixes);
            }
            replaceIgnorePrefixes.addAll(getDefaultReplaceIgnore());
        }
    }

    protected boolean isClassMatched(String targetClass) {
        return targetClasses.contains(targetClass);
    }

    protected boolean isMethodMatched(String targetMethod) {
        return targetMethods.contains(targetMethod);
    }

    protected boolean isFieldMatched(String targetField){
        return targetFields.contains(targetField);
    }

    protected boolean isIgnorePrefix(String invoker) {
        if (config.ignorePrefixes != null) {
            for (String prefix : config.ignorePrefixes) {
                if (invoker.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isReplaceEnable(){
        return config.replaceEnable;
    }

    protected IReplace getReplaceFieldTarget(String target){
       return replaceFieldTarget.get(target);
    }

    protected IReplace getReplaceMethodTarget(String target){
        return replaceMethodTarget.get(target);
    }

    protected boolean isReplaceIgnore(String invoker){
        for (String prefix : replaceIgnorePrefixes){
            if (invoker.startsWith(prefix)){
                return true;
            }
        }
        return false;
    }

    protected void printLabel(String label){
        FinderPlugin.log(label);
    }

    protected void print(String target, String className, String methodName, int lineNumber, File inputFile) {
        if (methodName == null || methodName.length() == 0) {
            FinderPlugin.log(
                    "\nMatched:" + target +
                            "\nLocation:" + className + "(" + lineNumber + ")" +
                            "\nFile:" + inputFile.getAbsolutePath());
        } else {
            FinderPlugin.log(
                    "\nMatched:" + target +
                            "\nLocation:" + className + "." + methodName + "(" + lineNumber + ")" +
                            "\nFile:" + inputFile.getAbsolutePath());
        }
    }

    protected void printReplace(String target, String replaceTarget,  String className, String methodName, int lineNumber, File inputFile){
        if (methodName == null || methodName.length() == 0) {
            FinderPlugin.log(
                    "\nMatched:" + target +
                            "\nReplaced:" + replaceTarget +
                            "\nLocation:" + className + "(" + lineNumber + ")" +
                            "\nFile:" + inputFile.getAbsolutePath());
        } else {
            FinderPlugin.log(
                    "\nMatched:" + target +
                            "\nReplaced:" + replaceTarget +
                            "\nLocation:" + className + "." + methodName + "(" + lineNumber + ")" +
                            "\nFile:" + inputFile.getAbsolutePath());
        }
    }

    public void execute(File inputFile) {
        this.execute(inputFile, null);
    }

    public void execute(File inputFile, ClassLoader classLoader) {
        this.inputFile = inputFile;

        try {
            JarFile jar = checkJar();
            if (jar == null) {
                scanDirectory();
            } else {
                scanJar(jar, inputFile, classLoader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JarFile checkJar() {
        if (inputFile.isDirectory() || !inputFile.getAbsolutePath().endsWith(".jar")) {
            return null;
        }
        try {
            return new JarFile(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scanJar(JarFile jar, File inputFile, ClassLoader classLoader) throws IOException {
        Enumeration<JarEntry> entries = jar.entries();

        File outputFile = new File("output.jar");

        if (outputFile.exists()){
            if (!outputFile.delete()){
                FinderPlugin.log("删除修改后的目录，未成功，终止操作");
                System.exit(0);
            }
        }
        // 目标输出文件
        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(outputFile));

        while (entries.hasMoreElements()) {

            JarEntry jarEntry = entries.nextElement();

            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                // 直接写回jar包
                ZipEntry result = new ZipEntry(jarEntry.getName());
                outputStream.putNextEntry(result);
                IOUtils.copy(jar.getInputStream(jarEntry), outputStream);
                outputStream.closeEntry();
                continue;
            }

            byte[] output = onAnalyzeJar(read(jar, jarEntry), classLoader);

            ZipEntry result = new ZipEntry(jarEntry.getName());
            outputStream.putNextEntry(result);
            outputStream.write(output);
            outputStream.closeEntry();
        }

        jar.close();
        outputStream.flush();
        outputStream.close();
        FileUtils.copyFile(outputFile, inputFile);
        outputFile.delete();
    }

    private void scanDirectory() {
        try {
            scan(inputFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private void scan(File target) {
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            if (files == null) {
                return;
            }
            for (File f : files) {
                scan(f);
            }
        } else {
            onAnalyze(read(target), target.getAbsolutePath());
        }
    }

    private byte[] read(JarFile jar, JarEntry entry) {
        InputStream is = null;
        try {
            is = jar.getInputStream(entry);
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    private byte[] read(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    abstract protected   HashMap<String, IReplace> getDefaultReplaceFieldTarget();
    abstract protected   HashMap<String, IReplace> getDefaultReplaceMethodTarget();

    private ArrayList<String> getDefaultReplaceIgnore(){
        ArrayList<String> replaceIgnore = new ArrayList<>();
        replaceIgnore.add("androidx/");
        replaceIgnore.add("android/");
        replaceIgnore.add("com/google");
        replaceIgnore.add("com/zxo/sample/BaseInfo");
        return replaceIgnore;
    }
}
