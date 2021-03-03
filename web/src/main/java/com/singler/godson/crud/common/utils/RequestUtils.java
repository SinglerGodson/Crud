package com.singler.godson.crud.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Request请求相关工具
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/3 11:26
 */
public class RequestUtils {

    public static <T> T paramsBean(HttpServletRequest request, Class<T> genericClass) throws BeanUtils.MapToBeanException {
        Map<String, String> paramsMap = RequestUtils.paramsMap(request);
        return BeanUtils.mapToBean(paramsMap, genericClass);
    }

    public static Map<String, String> paramsMap(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            paramsMap.put(name, request.getParameter(name));
        }
        return paramsMap;
    }

    public static Map<String, String> headersMap(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headersMap.put(name, request.getHeader(name));
        }
        return headersMap;
    }

    public static Map<String, String> cookiesMap(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies).collect(toMap(Cookie::getName, Cookie::getValue));
    }
}
