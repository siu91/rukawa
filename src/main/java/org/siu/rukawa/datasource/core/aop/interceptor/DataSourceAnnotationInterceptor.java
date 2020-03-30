package org.siu.rukawa.datasource.core.aop.interceptor;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.siu.rukawa.datasource.context.DynamicDataSourceContextHolder;
import org.siu.rukawa.datasource.core.anotation.DataSource;
import org.siu.rukawa.datasource.core.aop.handler.ChainHandler;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;


/**
 * 基于注解DataSource拦截处理
 *
 *
 * @Author Siu
 * @Date 2020/3/20 15:30
 * @Version 0.0.1
 * @see org.siu.rukawa.datasource.core.anotation.DataSource
 */
@Slf4j
public class DataSourceAnnotationInterceptor implements MethodInterceptor {

    @Setter
    ChainHandler dynamicChainHandler;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String key = doInvoke(invocation);
            // 切换数据源
            DynamicDataSourceContextHolder.push(key);
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }

    /**
     * 从注解中获取数据源标识
     *
     *
     * @param invocation
     * @return
     */
    private String doInvoke(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        DataSource dsAnnotation = method.isAnnotationPresent(DataSource.class) ? method.getAnnotation(DataSource.class)
                : AnnotationUtils.findAnnotation(invocation.getMethod().getDeclaringClass(), DataSource.class);
        String key = dsAnnotation == null ? "" : dsAnnotation.value();
        // 标识中以“#”开始表示动态处理
        return key.startsWith("#") ? dynamicChainHandler.handler(invocation, key) : key;

    }
}
