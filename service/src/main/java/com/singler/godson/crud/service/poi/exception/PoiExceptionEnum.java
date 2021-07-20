package com.singler.godson.crud.service.poi.exception;

import com.singler.godson.crud.common.exceptions.ExceptionInfo;
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

    ;


    private final Integer code;
    private final String desc;
}
