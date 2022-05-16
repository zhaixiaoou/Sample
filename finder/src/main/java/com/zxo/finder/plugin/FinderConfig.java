package com.zxo.finder.plugin;

import java.util.List;

/**
 * 插件扩展参数数据类
 */
public class FinderConfig {

    public List<String> classes;

    public List<String> methods;

    public List<String> fields;

    public List<String> ignorePrefixes;

    public List<String> ignoreReplacePrefixes;

    public boolean replaceEnable = false;
}
