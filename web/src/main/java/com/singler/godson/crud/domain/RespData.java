package com.singler.godson.crud.domain;

public class RespData {

    public static final Integer SUCCESS = 0;

    private Integer code;

    private String  msg;

    private Object data;

    /**
     * 用于错误信息构造
     *
     * @param code 状态码
     * @param msg 消息
     */
    private RespData(Integer code, String msg) {
        this.msg  = msg;
        this.code = code;
    }
    /**
     * 用于错误信息构造
     *
     * @param code 状态码
     * @param data 消息
     */
    private RespData(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 返回成功信息
     *
     * @param data 返回数据
     * @return
     */
    public static RespData success(Object data) {
        return new RespData(RespData.SUCCESS, data);
    }

    /**
     * 返回带数据的错误信息
     *
     * @param code 错误码
     * @param msg 错误信息
     * @return
     */
    public static RespData error(Integer code, String msg) {
        return new RespData(code, msg);
    }

    /**
     * 判断code是否为成功状态
     * @return
     */
    public boolean isSuccess() {
        return SUCCESS.equals(getCode());
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
