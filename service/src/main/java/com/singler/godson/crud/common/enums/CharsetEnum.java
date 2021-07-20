package com.singler.godson.crud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 编码枚举类，统一编码
 * @date 2020-05-21 16:40
 * @author lishunfei
 */
@Getter
@AllArgsConstructor
public enum CharsetEnum {
    ENCODED_TYPE_GBK( "GBK", "gbk编码"),
    ENCODED_TYPE_UTF8( "UTF-8", "中文编码"),
    ENCODED_TYPE_ISO_8859_1( "ISO-8859-1", "ISO编码"),

    ;
    /**
     * 编码
     */
    private String code;
    /**
     * 描述
     */
    private String description;
}
