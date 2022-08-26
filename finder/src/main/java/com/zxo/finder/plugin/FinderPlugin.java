package com.zxo.finder.plugin;


import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.AppExtension;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.build.gradle.internal.plugins.AppPlugin;
import com.android.build.gradle.internal.plugins.BasePlugin;
import com.android.build.gradle.internal.plugins.LibraryPlugin;
import com.android.build.gradle.internal.publishing.AndroidArtifacts;
import com.android.build.gradle.internal.scope.VariantScope;
import com.zxo.finder.plugin.asm.AsmFinderAnalyzer;
import com.zxo.finder.plugin.asm.AsmTraceAnalyzer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.PluginContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FinderPlugin implements Plugin<Project> {

    private static final String CONFIG_KEY = "finderConfig";
    private AbsFinderAnalyzer finderAnalyzer = new AsmFinderAnalyzer();
//    private AbsFinderAnalyzer finderAnalyzer = new AsmTraceAnalyzer();

    ///////////////////////////////////////////
    /// 统一日志处理
    ///////////////////////////////////////////
    public static void log(String content){
        System.out.println("["+FinderPlugin.class.getSimpleName()+"] "+ content);
    }

    ///////////////////////////////////////////
    /// Plugin
    ///////////////////////////////////////////
    @Override
    public void apply(Project target) {
       log("插件开始执行");
       target.getExtensions().create(CONFIG_KEY, FinderConfig.class);
       // 检查是否为Android项目或者android sdk
       Object android = target.getProperties().get("android");
       if (android instanceof BaseExtension){
           BaseExtension ext = (BaseExtension) android;
           ext.registerTransform(new FinderTransform(ext, target));
       } else {
           throw new IllegalStateException("'android' or 'android library' plugin required");
       }
    }


    ///////////////////////////////////////////
    /// Transform
    ///////////////////////////////////////////
    private class FinderTransform extends Transform{
        private BaseExtension android;
        private Project project;

        private FinderTransform(BaseExtension android, Project project){
            this.android = android;
            this.project = project;
        }

        @Override
        public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
            // 获取输入
            Collection<TransformInput> inputs = transformInvocation.getInputs();
            // 获取输出
            TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
            // 获取配置信息
            FinderConfig config = (FinderConfig) project.getProperties().get(CONFIG_KEY);
            try{
                finderAnalyzer.setConfig(config);
            } catch (Throwable e){
                e.printStackTrace();
            }

            //将所有依赖收集在一起当成classpath, 并添加道ClassLoader中，否则ASM会报找不到Class
            Set<URL> classPaths = new HashSet<>();
            addClassPath(classPaths);

            for (TransformInput i : inputs){
                Collection<DirectoryInput> dirs = i.getDirectoryInputs();
                for (DirectoryInput dir: dirs){
                    if (dir.getFile().isDirectory()){
                        classPaths.add(dir.getFile().toURI().toURL());
                        FinderPlugin.log("Directory File classpath="+dir.getFile().toURI().toURL());
                    }
                }

                Collection<JarInput> jars = i.getJarInputs();
                for (JarInput jar : jars){
                    if (!jar.getFile().isDirectory()) {
                        classPaths.add(jar.getFile().toURI().toURL());
                        FinderPlugin.log("Jar File classpath="+jar.getFile().toURI().toURL());
                    }
                }
            }

            finderAnalyzer.setClassLoader(URLClassLoader.newInstance(classPaths.toArray(new URL[classPaths.size()])));

            finderAnalyzer.setTemporaryDir(transformInvocation.getContext().getTemporaryDir());

            FinderPlugin.log("临时文件="+transformInvocation.getContext().getTemporaryDir().getAbsolutePath());
            // 处理文件夹
            File dirOutputFile = new File("../output.zip");
            if (dirOutputFile.exists()){
                dirOutputFile.delete();
            }

            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dirOutputFile));


            for (TransformInput i : inputs){

//                Collection<DirectoryInput> dirs = i.getDirectoryInputs();
//                for (DirectoryInput j: dirs){
//                    File input = j.getFile();
//                    File output = outputProvider.getContentLocation(j.getName(), j.getContentTypes(), j.getScopes(), Format.DIRECTORY);
//                    if (input == null){
//                        continue;
//                    }
//                    FinderPlugin.log("文件的input="+input.getAbsolutePath());
//                    FinderPlugin.log("文件的output="+output.getAbsolutePath());
//                    Stack<File> stackList= new Stack<>();
//
//                    if (input.isDirectory()){
//                        stackList.push(input);
//                        while (!stackList.isEmpty()){
//                            File dirFile = stackList.pop();
//                            if (dirFile.isDirectory()){
//                                buildZipDirEntry(dirFile, zos);
//                                File[] files = dirFile.listFiles();
//                                if (files != null && files.length > 0){
//                                    for (File file : files){
//                                        if (file.isDirectory()){
//                                            stackList.add(file);
//                                        } else {
//                                            buildZipEntry(file, zos);
//                                            finderAnalyzer.execute(file, output);
//                                        }
//                                    }
//                                }
//
//                            } else {
//                                buildZipEntry(dirFile, zos);
//                                finderAnalyzer.execute(dirFile, output);
//                            }
//
//                        }
//                    } else {
//                        buildZipEntry(input, zos);
//                        finderAnalyzer.execute(input, output);
//                    }
//
//                }

                Collection<JarInput> jars = i.getJarInputs();
                //  处理jar包
                for (JarInput j : jars){
                    File input = j.getFile();
                    File output = outputProvider.getContentLocation(j.getName(), j.getContentTypes(), j.getScopes(), Format.JAR);

//                    if (input.getAbsolutePath().endsWith("BaiduLBS_Android.jar")){
//                        FinderPlugin.log("jar包的input="+input.getAbsolutePath());
//                        FinderPlugin.log("jar包的output="+output.getAbsolutePath());
//                        finderAnalyzer.execute(input, output);
//                    } else {
//                        FileUtils.copyFile(input, output);
//                    }
                    FileUtils.copyFile(input, output);
                }

                // 处理文件夹
                Collection<DirectoryInput> dirs = i.getDirectoryInputs();
                for (DirectoryInput j: dirs){
                    File input = j.getFile();
                    File output = outputProvider.getContentLocation(j.getName(), j.getContentTypes(), j.getScopes(), Format.DIRECTORY);
                    if (input == null){
                        continue;
                    }
                    FinderPlugin.log("文件的input="+input.getAbsolutePath());
                    FinderPlugin.log("文件的output="+output.getAbsolutePath());
                    buildZip(input, zos, "");
                    finderAnalyzer.execute(input, output);
                }


            }

            zos.close();

        }


        private void buildZip(File input, ZipOutputStream zos, String parentFileName) {

            String tmpFileName ;
            try {
                if (input.isDirectory()){
                    tmpFileName = parentFileName + input.getName()+File.separator;
                    ZipEntry entry = new ZipEntry(tmpFileName);
                    zos.putNextEntry(entry);
                    File[] files = input.listFiles();
                    for (File file : files){
                        buildZip(file, zos, tmpFileName);
                    }
                } else {
                    tmpFileName = parentFileName + input.getName();
                    ZipEntry entry = new ZipEntry(tmpFileName);
                    zos.putNextEntry(entry);
                    zos.write(FileUtils.readFileToByteArray(input));
                    zos.closeEntry();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private final  String[] SUPPORT_REGEX = {
                ".*[Ii]mplementation$",
                ".*[Aa]pi$",
                ".*[Cc]ompileOnly$",
                ".*[Pp]rovided$",
        };

        private void addClassPath(Set<URL> classPaths) {
            List<File> bootClassPaths = android.getBootClasspath();
            for (File f: bootClassPaths){
                try {
                    classPaths.add(f.toURI().toURL());
                    FinderPlugin.log("BootClasspath File classpath="+f.toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            ConfigurationContainer cc = project.getConfigurations();
            for (Configuration i : cc){
                PluginContainer pc = project.getPlugins();
                BasePlugin plugin = pc.hasPlugin(AppPlugin.class) ? pc.findPlugin(AppPlugin.class) : pc.findPlugin(LibraryPlugin.class);
                if (plugin == null){
                    FinderPlugin.log("Could not found AppPlugin or LibraryPlugin");
                    return ;
                }

                String name = i.getName();
                FinderPlugin.log("Configuration name="+name);
                for (String regex: SUPPORT_REGEX){
                    if (!name.matches(regex)){
                        continue;
                    }
                    List<VariantScope> scopes = plugin.getVariantManager().getVariantScopes();
                    for (VariantScope scope: scopes){
                        FileCollection fileCollection = scope.getJavaClasspath(AndroidArtifacts.ConsumedConfigType.COMPILE_CLASSPATH, AndroidArtifacts.ArtifactType.CLASSES, null);
                        for (File f: fileCollection){
                            try {
                                classPaths.add(f.toURI().toURL());
                                FinderPlugin.log("Java File classpath="+f.toURI().toURL());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                        return;
                    }
                }

            }
        }

        @Override
        public String getName() {
            return getClass().getSimpleName();
        }

        @Override
        public Set<QualifiedContent.ContentType> getInputTypes() {
            return TransformManager.CONTENT_JARS;
        }

        @Override
        public Set<? super QualifiedContent.Scope> getScopes() {
            if (android instanceof AppExtension){
                return TransformManager.SCOPE_FULL_PROJECT;
            }  else {
                return TransformManager.PROJECT_ONLY;
            }
        }

        @Override
        public boolean isIncremental() {
            return false;
        }
    }


}
