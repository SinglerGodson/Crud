package com.singler.godson.crud.common.enums;

/**
 * 布尔值枚举类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/10 13:57
 */
public enum BooleanEnum {
    TRUE (1, "1", "是"),
    FALSE(0, "0", "否");

    private Integer integer;
    private String  string;
    private String  desc;

    BooleanEnum(Integer integer, String string, String desc) {
        this.desc = desc;
        this.string  = string;
        this.integer = integer;
    }

    public Integer intValue() {
        return integer;
    }

    public String strValue() {
        return string;
    }

    public Short shortValue() {
        return integer.shortValue();
    }

    public String desc() {
        return desc;
    }
}
