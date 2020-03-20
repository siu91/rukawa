package org.siu.rukawa.datasource.core.aop.handler;


import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 从请求头中获取数据源名称
 *
 * @Author Siu
 * @Date 2020/3/19 22:33
 * @Version 0.0.1
 */
public class RequestHeaderDataSourceHandler extends AbstractDataSourceChainHandler {

    @Override
    public boolean matcher(String key) {
        return StringUtils.hasText(key) && key.startsWith("#header");
    }

    @Override
    public String fetchDataSourceName(MethodInvocation invocation, String key) {
        ServletRequestAttributes request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return request == null ? null : request.getRequest().getHeader(key.substring(8));
    }
}
