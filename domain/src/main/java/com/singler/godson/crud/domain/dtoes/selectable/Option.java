package com.singler.godson.crud.domain.dtoes.selectable;

import java.io.Serializable;

/**
 * 下拉列表选项对象
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/10/29 15:26
 */
public class Option {
    private String desc;
    private Serializable code;
    private Serializable parentCode;

    public Option() {}

    public Option(Serializable code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Option(Serializable code, String desc, String parentCode) {
        this.code = code;
        this.desc = desc;
        this.parentCode = parentCode;
    }

    public Serializable getCode() {
        return code;
    }

    public void setCode(Serializable code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Serializable getParentCode() {
        return parentCode;
    }

    public void setParentCode(Serializable parentCode) {
        this.parentCode = parentCode;
    }
}
