package com.singler.godson.crud.common.exceptions;

/**
 * 基础异常列
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/5/24 19:58
 */
public class CrudException extends RuntimeException {

    private int code;
    private String desc;

    private CrudException(){}

    public CrudException(ExceptionInfo exceptionInfo) {

    }

    public CrudException(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
