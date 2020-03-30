package org.siu.rukawa.datasource.core.aop.pointcut;

import org.siu.rukawa.datasource.core.cache.MatcherCache;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

import javax.validation.constraints.NotNull;

/**
 * @Author Siu
 * @Date 2020/3/29 15:48
 * @Version 0.0.1
 */
public class CustomJdkRegexpPointcut extends JdkRegexpMethodPointcut {

    MatcherCache cache;

    private String ds;

    public CustomJdkRegexpPointcut(MatcherCache cache, String ds, String... pattern) {
        this.cache = cache;
        this.ds = ds;
        super.setPatterns(pattern);

    }

    @Override
    protected boolean matches(@NotNull String pattern, int patternIndex) {
        boolean matches = super.matches(pattern, patternIndex);
        if (matches) {
            cache.set(pattern, this.ds);
        }
        return matches;
    }
}
