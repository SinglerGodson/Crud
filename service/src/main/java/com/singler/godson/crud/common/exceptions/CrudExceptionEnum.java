package com.singler.godson.crud.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 增删改查异常信息
 *
 * @author maenfang1
 * @date 2021/7/20 22:40
 */
@Getter
@AllArgsConstructor
public enum CrudExceptionEnum implements ExceptionInfo {

    EMPTY_ENTITY("2001", "参数不能为空"),

    ;

    private final String code;
    private final String desc;
}
