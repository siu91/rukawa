package org.siu.rukawa.datasource.core.aop.handler;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

/**
 * 抽象数据源处理
 *
 * @Author Siu
 * @Date 2020/3/18 16:42
 * @Version 0.0.1
 */
public abstract class AbstractDataSourceChainHandler implements ChainHandler {

    private ChainHandler nextHandler;

    public void setNextHandler(ChainHandler nextHandler) {
        this.nextHandler = nextHandler;
    }


    @Override
    public String handler(MethodInvocation invocation, String key) {
        if (matcher(key)) {
            String ds = fetchDataSourceName(invocation, key);
            if (!StringUtils.hasText(ds) && nextHandler != null) {
                return nextHandler.handler(invocation, key);
            }
            return ds;
        }
        // 不匹配，进入下一个handler
        if (nextHandler != null) {
            return nextHandler.handler(invocation, key);
        }
        return null;
    }

    /**
     * 检查是否匹配，不匹配则跳过进入下一个处理器
     *
     * @param key
     * @return
     */
    public abstract boolean matcher(String key);

    /**
     * 处理datasource
     *
     * @param invocation
     * @param key
     * @return
     */
    public abstract String fetchDataSourceName(MethodInvocation invocation, String key);


}
