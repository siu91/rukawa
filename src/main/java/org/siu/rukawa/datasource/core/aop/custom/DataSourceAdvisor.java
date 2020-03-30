package org.siu.rukawa.datasource.core.aop.custom;

import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.siu.rukawa.datasource.core.cache.MatcherCache;
import org.siu.rukawa.datasource.core.anotation.DataSource;
import org.siu.rukawa.datasource.core.aop.custom.interceptor.CustomDataSourceInterceptor;
import org.siu.rukawa.datasource.core.aop.custom.matcher.ExpressionMatcher;
import org.siu.rukawa.datasource.core.aop.custom.matcher.Matcher;
import org.siu.rukawa.datasource.core.aop.custom.matcher.RegexMatcher;
import org.siu.rukawa.datasource.core.aop.custom.pointcut.CustomAspectJExpressionPointcut;
import org.siu.rukawa.datasource.core.aop.custom.pointcut.CustomJdkRegexpPointcut;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.util.List;

/**
 * AOP 处理DataSource注解
 *
 * @Author Siu
 * @Date 2020/3/20 16:02
 * @Version 0.0.1
 * @see DataSource
 */
public class DataSourceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private MatcherCache cache;

    private Advice advice;

    private Pointcut pointcut;

    public DataSourceAdvisor(@NonNull CustomDataSourceInterceptor interceptor, @NonNull MatcherCache cache, @NonNull List<Matcher> matchers) {
        this.advice = interceptor;
        this.cache = cache;
        // 设置切点
        this.pointcut = this.buildPointcut(matchers);

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

    private Pointcut buildPointcut(List<Matcher> matchers) {
        ComposablePointcut composablePointcut = null;
        for (Matcher matcher : matchers) {
            if (matcher instanceof RegexMatcher) {
                RegexMatcher regexMatcher = (RegexMatcher) matcher;
                Pointcut pointcut = new CustomJdkRegexpPointcut(cache, regexMatcher.getDs(), regexMatcher.getPatterns());
                if (composablePointcut == null) {
                    composablePointcut = new ComposablePointcut(pointcut);
                } else {
                    composablePointcut.union(pointcut);
                }
            } else {
                ExpressionMatcher expressionMatcher = (ExpressionMatcher) matcher;
                Pointcut pointcut = new CustomAspectJExpressionPointcut(cache, expressionMatcher.getExpression(), expressionMatcher.getDs());
                if (composablePointcut == null) {
                    composablePointcut = new ComposablePointcut(pointcut);
                } else {
                    composablePointcut.union(pointcut);
                }
            }
        }
        return composablePointcut;
    }
}
