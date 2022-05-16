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
import com.zxo.finder.plugin.asm.AsmFinderAnalyzer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class FinderPlugin implements Plugin<Project> {

    private static final String CONFIG_KEY = "finderConfig";
    private AbsFinderAnalyzer finderAnalyzer = new AsmFinderAnalyzer();

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


            for (TransformInput i : inputs){
                Collection<JarInput> jars = i.getJarInputs();
                //  处理jar包
                for (JarInput j : jars){
                    File input = j.getFile();
                    File output = outputProvider.getContentLocation(j.getName(), j.getContentTypes(), j.getScopes(), Format.JAR);
//                    finderAnalyzer.execute(input);
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
                    // 通过LinkedList循序遍历文件夹，直到找到文件
                    LinkedList<File> stack = new LinkedList<>();
                    stack.offer(input);
                    while (!stack.isEmpty()){
                        input = stack.poll();
                        if (input.isDirectory()){
                            File[] files = input.listFiles();
                            if (files == null){
                                continue;
                            }
                            for (File f: files){
                                stack.offer(f);
                            }
                        } else {
                            finderAnalyzer.execute(input);
                        }
                    }
                    FileUtils.copyDirectory(j.getFile(), output);
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
