package org.siu.rukawa.datasource.core.aop.custom.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @Author Siu
 * @Date 2020/3/29 15:46
 * @Version 0.0.1
 */
@AllArgsConstructor
@Data
public class RegexMatcher implements Matcher {

    private String[] patterns;

    private String ds;
}
