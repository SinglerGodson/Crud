package com.singler.godson.crud.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * POI相关异常信息
 *
 * @author maenfang1
 * @date 2021/7/20 21:37
 */
@Getter
@AllArgsConstructor
public enum PoiExceptionEnum implements ExceptionInfo {

    NO_PROPERTIES("3001", "未找到%s配置项"),
    WRONG_TYPE("3002", "文档格式不正确"),
    ;


    private final String code;
    private final String desc;
}
