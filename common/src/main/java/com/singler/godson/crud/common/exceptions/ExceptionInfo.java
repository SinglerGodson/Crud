package com.singler.godson.crud.common.exceptions;

/**
 * 异常信息接口
 *
 * @author maenfang1
 * @date 2021/7/8 17:44
 */
public interface ExceptionInfo {

    /**
     * 异常编码
     * @return
     */
    String getCode();

    /**
     * 异常说明
     * @return
     */
    String getDesc();
}
