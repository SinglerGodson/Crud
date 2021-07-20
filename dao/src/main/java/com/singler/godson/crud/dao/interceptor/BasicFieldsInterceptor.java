package com.singler.godson.crud.dao.interceptor;

import com.singler.godson.crud.domain.gateway.LoginUserInfo;
import com.singler.godson.hibatis.builder.HibatisConfiguration;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 基础字段处理拦截器
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/5/12 22:26
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query" , args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query" , args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
@Component
public class BasicFieldsInterceptor implements Interceptor {
    /** 账号 **/
    public static final String ACCOUNT_ID   = "accountId";
    /** 创建人 **/
    public static final String CREATOR_ID   = "creatorId";
    /** 修改人 **/
    public static final String MODIFIER_ID  = "modifierId";
    /** 平台id **/
    public static final String PLATFORM_ID  = "platformId";

    private static final ThreadLocal<LoginUserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object arg = invocation.getArgs()[0];
        if (arg instanceof MappedStatement) {
            MappedStatement ms = (MappedStatement) arg;
            if(ms.getConfiguration() instanceof HibatisConfiguration) {
                HibatisConfiguration configuration = (HibatisConfiguration) ms.getConfiguration();
                if (configuration.hibatisHandled(ms.getId())) {
                    setBasicFields(invocation);
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 设置基础属性
     * @param invocation
     */
    private void setBasicFields(Invocation invocation) {
        LoginUserInfo loginUserInfo = getThreadLocal().get();
        if (loginUserInfo != null) {
            Object args = invocation.getArgs()[1];
            if (args == null) {
                args = new HashMap<String, Object>(4);
                invocation.getArgs()[1] = args;
            }
            if (args instanceof Map) {
                Map<String, Object> argsMap = (Map<String, Object>) args;
                if (loginUserInfo.getAccountId() != null) {
                    argsMap.putIfAbsent(CREATOR_ID, loginUserInfo.getAccountId());
                    argsMap.putIfAbsent(ACCOUNT_ID, loginUserInfo.getAccountId());
                    argsMap.putIfAbsent(MODIFIER_ID, loginUserInfo.getAccountId());
                }
                if (loginUserInfo.getPlatformId() != null) {
                    argsMap.putIfAbsent(PLATFORM_ID, loginUserInfo.getPlatformId());
                }
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private static ThreadLocal<LoginUserInfo> getThreadLocal() {
        return THREAD_LOCAL;
    }

    public static void remove() {
        getThreadLocal().remove();
    }

    public static void setLoginUserInfo(LoginUserInfo loginUserInfo) {
        getThreadLocal().set(loginUserInfo);
    }
}
