package com.singler.godson.crud.common.exceptions;

/**
 * 基础异常列
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/5/24 19:58
 */
public class CrudException extends RuntimeException {

    private final String code;
    private final String desc;

    private CrudException() {
        this.code = null;
        this.desc = null;
    }

    public CrudException(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public CrudException(ExceptionInfo exceptionInfo, Object... values) {
        this(exceptionInfo.getCode(), String.format(exceptionInfo.getDesc(), values));
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
