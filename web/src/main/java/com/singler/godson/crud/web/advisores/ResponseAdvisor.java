package com.singler.godson.crud.web.advisores;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.jd.ecc.commons.web.model.RespData;
import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.common.utils.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 *  Controller返回数据类型转换
 *
 * @author maenfang1
 * @version 1.0
 * @date 2019/9/16 21:56
 */
@ControllerAdvice // (annotations = RespDataController.class)
public class ResponseAdvisor implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter parameter, Class<? extends HttpMessageConverter<?>> httpMessageConverterClass) {
        Class<?> returnType = parameter.getParameterType().getClass();
        // 转换返回值类型不是RespData的返回结果。
        return returnType != RespData.class;
    }

    @Override
    public Object beforeBodyWrite(Object respBody, MethodParameter parameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> httpMessageConverterClass,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (StringHttpMessageConverter.class.equals(httpMessageConverterClass)) {
            // 因为StringHttpMessageConverter会直接把字符串写入body, 所以字符串特殊处理
            return JsonUtils.toJson(RespData.success(respBody));
        } else if (respBody instanceof Page) {
            respBody = new PageInfo((Page) respBody);
        }
        return RespData.success(respBody);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RespData errorHandler(Exception ex) {
        if (ex instanceof CrudException) {
            CrudException crudException = (CrudException) ex;
            return RespData.error(crudException.getCode(), crudException.getDesc());
        }
        return RespData.error("" + HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
