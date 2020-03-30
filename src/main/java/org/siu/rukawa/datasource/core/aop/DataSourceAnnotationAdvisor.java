package org.siu.rukawa.datasource.core.aop;

import lombok.NonNull;
import org.siu.rukawa.datasource.core.anotation.DataSource;
import org.siu.rukawa.datasource.core.aop.interceptor.DataSourceAnnotationInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * AOP 处理DataSource注解
 *
 * @Author Siu
 * @Date 2020/3/20 16:02
 * @Version 0.0.1
 * @see org.siu.rukawa.datasource.core.anotation.DataSource
 */
public class DataSourceAnnotationAdvisor extends AbstractRukawaAdvisor {


    public DataSourceAnnotationAdvisor(@NonNull DataSourceAnnotationInterceptor interceptor) {
        super(interceptor, null);
        this.advice = interceptor;
    }


    @Override
    public Pointcut buildPointcut() {
        // 设置切点
        Pointcut classLevelPointCut = new AnnotationMatchingPointcut(DataSource.class, true);
        Pointcut methodLevelPointCut = AnnotationMatchingPointcut.forMethodAnnotation(DataSource.class);
        return new ComposablePointcut(classLevelPointCut).union(methodLevelPointCut);
    }
}
