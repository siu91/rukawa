package org.siu.rukawa.datasource.autoconfigure.properties;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author Siu
 * @Date 2020/3/29 16:23
 * @Version 0.0.1
 */
@Data
@Accessors(chain = true)
public class CustomPointcutProperty {

    private String ds;

    /**
     * 自定义切点的类型 ：REGEXP 、EXPRESSION
     */
    private CustomPointcut type;

    /**
     * 表达式或正则匹配
     */
    private List<String> matchers;
}
