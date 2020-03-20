package org.siu.rukawa.datasource.core.aop.handler;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 链式处理
 *
 * @Author Siu
 * @Date 2020/3/18 16:37
 * @Version 0.0.1
 */
public interface ChainHandler {

    /**
     * 处理注解中的数据源标识
     *
     * @param invocation
     * @param key
     * @return
     */
    String handler(MethodInvocation invocation, String key);
}
