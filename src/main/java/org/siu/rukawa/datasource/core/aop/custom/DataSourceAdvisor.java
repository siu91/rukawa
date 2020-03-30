package org.siu.rukawa.datasource.core.aop.custom;

import lombok.NonNull;
import org.siu.rukawa.datasource.core.aop.AbstractRukawaAdvisor;
import org.siu.rukawa.datasource.core.cache.MatcherCache;
import org.siu.rukawa.datasource.core.anotation.DataSource;
import org.siu.rukawa.datasource.core.aop.custom.interceptor.CustomDataSourceInterceptor;
import org.siu.rukawa.datasource.core.aop.custom.matcher.ExpressionMatcher;
import org.siu.rukawa.datasource.core.aop.custom.matcher.Matcher;
import org.siu.rukawa.datasource.core.aop.custom.matcher.RegexMatcher;
import org.siu.rukawa.datasource.core.aop.custom.pointcut.CustomAspectJExpressionPointcut;
import org.siu.rukawa.datasource.core.aop.custom.pointcut.CustomJdkRegexpPointcut;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;

import java.util.List;

/**
 * AOP 处理DataSource注解
 *
 * @Author Siu
 * @Date 2020/3/20 16:02
 * @Version 0.0.1
 * @see DataSource
 */
public class DataSourceAdvisor extends AbstractRukawaAdvisor {

    private List<Matcher> matchers;

    public DataSourceAdvisor(@NonNull CustomDataSourceInterceptor interceptor, @NonNull MatcherCache cache, @NonNull List<Matcher> matchers) {
        super(interceptor, cache);
        this.matchers = matchers;

    }

    @Override
    public Pointcut buildPointcut() {
        return buildPointcut(this.matchers);
    }

    /**
     * 构建切点
     *
     * @param matchers
     * @return
     */
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
