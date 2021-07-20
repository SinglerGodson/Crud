package com.singler.godson.crud.web.filters;

import com.singler.godson.crud.common.utils.JsonUtils;
import com.singler.godson.crud.common.utils.RequestUtils;
import com.singler.godson.crud.dao.interceptor.BasicFieldsInterceptor;
import com.singler.godson.crud.domain.gateway.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * xss过滤器
 * @author baibing
 * @version 1.0
 */
@Slf4j
@Order(1)
@WebFilter(filterName = "BasicFieldsFilter", urlPatterns = "/*", asyncSupported = true)
public class BasicFieldsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("BasicFieldsFilter init.....");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String accountId = request.getParameter(BasicFieldsInterceptor.ACCOUNT_ID);
        String platformId = request.getParameter(BasicFieldsInterceptor.PLATFORM_ID);
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        if (!StringUtils.isEmpty(accountId)) {
            loginUserInfo.setAccountId(Long.parseLong(accountId));
        }
        if (!StringUtils.isEmpty(platformId)) {
            loginUserInfo.setPlatformId(Long.parseLong(platformId));
        }
        BasicFieldsInterceptor.setLoginUserInfo(loginUserInfo);
        chain.doFilter(request, response);
        log.info("BasicFieldsInterceptor.remove(): getContextPath: {}", request.getServletContext().getContextPath());
        BasicFieldsInterceptor.remove();
    }

}
