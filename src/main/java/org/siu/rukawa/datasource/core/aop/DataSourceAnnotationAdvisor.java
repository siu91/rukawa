package org.siu.rukawa.datasource.core.aop;

import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.siu.rukawa.datasource.core.anotation.DataSource;
import org.siu.rukawa.datasource.core.aop.interceptor.DataSourceAnnotationInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * AOP 处理DataSource注解
 *
 * @Author Siu
 * @Date 2020/3/20 16:02
 * @Version 0.0.1
 * @see org.siu.rukawa.datasource.core.anotation.DataSource
 */
public class DataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    public DataSourceAnnotationAdvisor(@NonNull DataSourceAnnotationInterceptor interceptor) {
        this.advice = interceptor;
        // 设置切点
        Pointcut classLevelPointCut = new AnnotationMatchingPointcut(DataSource.class, true);
        Pointcut methodLevelPointCut = AnnotationMatchingPointcut.forMethodAnnotation(DataSource.class);
        this.pointcut = new ComposablePointcut(classLevelPointCut).union(methodLevelPointCut);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }
}
