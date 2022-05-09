package com.zxo.finder.plugin;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 分析字节码基础类
 */
public abstract class AbsFinderAnalyzer {

    abstract protected void onAnalyze(byte[] byteCodes, String absolutePath);

    private FinderConfig config;
    private final HashSet<String> targetClasses = new HashSet<>();
    private final HashSet<String> targetMethods = new HashSet<>();

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
    }

    protected boolean isClassMatched(String targetClass) {
        return targetClasses.contains(targetClass);
    }

    protected boolean isMethodMatched(String targetMethod) {
        return targetMethods.contains(targetMethod);
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


    public void execute(File inputFile) {
        this.inputFile = inputFile;

        try {
            JarFile jar = checkJar();
            if (jar == null) {
                scanDirectory();
            } else {
                scanJar(jar);
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

    private void scanJar(JarFile jar) throws IOException {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                continue;
            }
            onAnalyze(read(jar, jarEntry), "");
        }
        jar.close();
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

}
