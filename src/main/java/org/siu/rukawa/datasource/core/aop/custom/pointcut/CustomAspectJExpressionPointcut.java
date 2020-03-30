package org.siu.rukawa.datasource.core.aop.custom.pointcut;

import org.siu.rukawa.datasource.core.cache.MatcherCache;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

/**
 * @Author Siu
 * @Date 2020/3/29 16:02
 * @Version 0.0.1
 */
public class CustomAspectJExpressionPointcut extends AspectJExpressionPointcut {

    private MatcherCache cache;

    private String ds;

    public CustomAspectJExpressionPointcut(MatcherCache cache, String expression, String ds) {
        this.cache = cache;
        this.ds = ds;
        super.setExpression(expression);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions) {
        boolean matches = super.matches(method, targetClass, hasIntroductions);
        if (matches) {
            cache.set(targetClass.getName().concat(".").concat(method.getName()), ds);
        }

        return matches;
    }
}
