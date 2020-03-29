package org.siu.rukawa.datasource.core.aop.handler;

import org.siu.rukawa.datasource.autoconfigure.properties.ChainHandlerOrder;

/**
 * 处理顺序
 *
 * @Author Siu
 * @Date 2020/3/27 21:18
 * @Version 0.0.1
 */
public class ChainHandlerBuilder {


    public static AbstractDataSourceChainHandler buildChainHandler(ChainHandlerOrder order) {
        AbstractDataSourceChainHandler chainHandler;
        AbstractDataSourceChainHandler requestHeaderDataSourceHandler = new HeaderDataSourceHandler();
        AbstractDataSourceChainHandler sessionDataSourceHandler = new SessionDataSourceHandler();
        AbstractDataSourceChainHandler spELDataSourceHandler = new SpELDataSourceHandler();
        switch (order) {
            case HEADER_SPEL:
                chainHandler = requestHeaderDataSourceHandler;
                chainHandler.setNextHandler(spELDataSourceHandler);
                break;
            case SESSION_SPEL:
                chainHandler = sessionDataSourceHandler;
                chainHandler.setNextHandler(spELDataSourceHandler);
                break;
            case SESSION_HEADER_SPEL:
                chainHandler = sessionDataSourceHandler;
                requestHeaderDataSourceHandler.setNextHandler(spELDataSourceHandler);
                chainHandler.setNextHandler(requestHeaderDataSourceHandler);
                break;
            default:
                // 默认 header>session>SpEL
                chainHandler = requestHeaderDataSourceHandler;
                sessionDataSourceHandler.setNextHandler(spELDataSourceHandler);
                chainHandler.setNextHandler(sessionDataSourceHandler);
        }

        return chainHandler;

    }
}
