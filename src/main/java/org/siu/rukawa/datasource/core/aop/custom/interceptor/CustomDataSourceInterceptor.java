package org.siu.rukawa.datasource.core.aop.custom.interceptor;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.siu.rukawa.datasource.cache.MatcherCache;
import org.siu.rukawa.datasource.context.DynamicDataSourceContextHolder;
import org.siu.rukawa.datasource.core.anotation.DataSource;
import org.siu.rukawa.datasource.core.aop.handler.ChainHandler;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;


/**
 * 自定义的数据源标识处理
 * 1、从匹配的缓存取
 * 2、从内置的处理器中获取（header>session>SePL）
 *
 * @Author Siu
 * @Date 2020/3/20 15:30
 * @Version 0.0.1
 * @see DataSource
 */
@Slf4j
public class CustomDataSourceInterceptor implements MethodInterceptor {

    @Setter
    ChainHandler dynamicChainHandler;

    @Setter
    MatcherCache cache;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Method method = invocation.getMethod();
            String methodPath = invocation.getThis().getClass().getName().concat(".").concat(method.getName());
            String key = cache.get(methodPath);

            if (key != null && !key.isEmpty() && key.startsWith("#")) {
                key = dynamicChainHandler.handler(invocation, key);
            }
            DynamicDataSourceContextHolder.push(key);
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }


}
