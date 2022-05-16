package com.zxo.finder.plugin.asm;

import com.zxo.finder.plugin.IReplace;

/**
 * asm具体要替换的数据类
 */
public class ASMReplaceBean  implements IReplace {
    // 类名
    public String owner;
    // 方法名或属性值
    public String name;
    // 返回值
    public String descriptor;

    public ASMReplaceBean(String owner, String name, String descriptor) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
    }

    public ASMReplaceBean() {
    }
}
