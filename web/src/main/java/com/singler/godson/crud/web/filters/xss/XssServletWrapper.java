package com.singler.godson.crud.web.filters.xss;

import com.singler.godson.crud.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author baibing
 * @version 1.0
 * @since 2017-07-17 19:58
 */
public class XssServletWrapper extends HttpServletRequestWrapper {
    public XssServletWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        return filterXss(super.getHeader(name));
    }

    @Override
    public String getParameter(String name) {
        return filterXss(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                values[i] = filterXss(values[i]);
            }
        }
        return values;
    }

    /**
     * 常见注入方式，一般是script和on*的操作，讨论后过滤掉
     * <a href="javascript:alert(1)" ></a>
     * <iframe src="javascript:alert(1)" />
     * <img src='x' onerror="alert(1)" />
     * <video src='x' onerror="alert(1)" ></video>
     * <div onclick="alert(1)" onmouseover="alert(2)" ><div>
     * @param value
     * @return
     */
    private String filterXss(String value) {
        if (!StringUtils.isEmpty(value)) {
            value = value.replaceAll("<javascript", "&lt;javascript")
                         .replaceAll("<script", "&lt;script")
                         .replaceAll("onerror", "on error")
                         .replaceAll("onmouseover", "on mouseover")
                         .replaceAll("mousedown", "mouse down")
                         .replaceAll("mouseup", "mouse up")
                         .replaceAll("click", "cli ck")
                         .replaceAll("dbclick", "db click")
                         .replaceAll("contextmenu", "context menu")
                         .replaceAll("mouseout", "mouse out")
                         .replaceAll("mousemove", "mouse move")
                         .replaceAll("mousedown", "mouse down")
                         .replaceAll("<iframe", "&lt;iframe")
                         .replaceAll("</iframe>", "&lt;iframe:&gt;");
//                       .replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//                       .replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
//                       .replaceAll("'", "& #39;");
//                       .replaceAll("eval\\((.*)\\)", "");
//                       .replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//                       .replaceAll("script", "");
        }
        return value;
    }
}
