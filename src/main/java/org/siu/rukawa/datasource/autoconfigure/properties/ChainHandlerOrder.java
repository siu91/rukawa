package org.siu.rukawa.datasource.autoconfigure.properties;

/**
 * aop 处理的顺序
 * 默认 header>session>SpEL
 *
 * @Author Siu
 * @Date 2020/3/27 21:09
 * @Version 0.0.1
 */
public enum ChainHandlerOrder {

    /**
     * 处理顺序
     */
    HEADER_SESSION_SEPL,
    SESSION_HEADER_SPEL,
    SESSION_SPEL,
    HEADER_SPEL


}
