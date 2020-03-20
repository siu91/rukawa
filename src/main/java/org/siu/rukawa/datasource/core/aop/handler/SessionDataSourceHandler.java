package org.siu.rukawa.datasource.core.aop.handler;


import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 从Session中获取数据源名称
 *
 * @Author Siu
 * @Date 2020/3/19 22:33
 * @Version 0.0.1
 */
public class SessionDataSourceHandler extends AbstractDataSourceChainHandler {

    @Override
    public boolean matcher(String key) {
        return StringUtils.hasText(key) && key.startsWith("#session");
    }

    @Override
    public String fetchDataSourceName(MethodInvocation invocation, String key) {
        ServletRequestAttributes request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return request == null ? null : request.getRequest().getSession().getAttribute(key.substring(9)).toString();
    }
}
