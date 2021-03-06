package org.siu.rukawa.datasource.core.aop.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author Siu
 * @Date 2020/3/29 15:46
 * @Version 0.0.1
 */
@AllArgsConstructor
@Data
public class ExpressionMatcher implements Matcher {

    private String expression;

    private String ds;
}
