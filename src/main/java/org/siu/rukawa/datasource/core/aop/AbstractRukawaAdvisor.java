package org.siu.rukawa.datasource.core.aop;

import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.siu.rukawa.datasource.core.anotation.DataSource;
import org.siu.rukawa.datasource.core.cache.MatcherCache;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


/**
 * AOP 处理DataSource注解
 *
 * @Author Siu
 * @Date 2020/3/20 16:02
 * @Version 0.0.1
 * @see DataSource
 */
public abstract class AbstractRukawaAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    protected MatcherCache cache;

    protected Advice advice;

    protected Pointcut pointcut;

    public AbstractRukawaAdvisor(@NonNull MethodInterceptor interceptor, MatcherCache cache) {
        this.advice = interceptor;
        this.cache = cache;
        // 设置切点
        this.pointcut = this.buildPointcut();

    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    /**
     * 实现BeanFactoryAware的bean中获取beanFactory
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    /**
     * 构建切面
     *
     * @return
     */
    public abstract Pointcut buildPointcut();

}
