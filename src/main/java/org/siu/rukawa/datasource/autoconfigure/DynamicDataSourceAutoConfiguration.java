package org.siu.rukawa.datasource.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.siu.rukawa.datasource.autoconfigure.properties.DynamicDataSourceProperties;
import org.siu.rukawa.datasource.core.aop.DataSourceAnnotationAdvisor;
import org.siu.rukawa.datasource.core.aop.handler.*;
import org.siu.rukawa.datasource.core.aop.interceptor.DataSourceAnnotationInterceptor;
import org.siu.rukawa.datasource.core.provider.DataSourceProvider;
import org.siu.rukawa.datasource.core.provider.YmlDataSourceProvider;
import org.siu.rukawa.datasource.core.provider.builder.DataSourceBuilder;
import org.siu.rukawa.datasource.core.provider.warp.AbstractDataSourceWarp;
import org.siu.rukawa.datasource.core.provider.warp.DataSourceWarp;
import org.siu.rukawa.datasource.core.provider.warp.P6spyDataSourceWarp;
import org.siu.rukawa.datasource.core.provider.warp.SeataDataSourceWarp;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 自动配置
 *
 * @Author Siu
 * @Date 2020/3/18 22:23
 * @Version 0.0.1
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration {

    private final DynamicDataSourceProperties properties;

    /**
     * 配置数据源构造工具
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DataSourceBuilder dataSourceBuilder() {
        return new DataSourceBuilder(properties);
    }


    /**
     * 数据源包装器：用来支持其它的数据源插件
     * 如：p6spy sql 监控，ali seata 分布式事务
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DataSourceWarp dataSourceWarp() {
        // 设置所有已支持的warp
        AbstractDataSourceWarp p6spyDataSourceWarp = new P6spyDataSourceWarp();
        p6spyDataSourceWarp.setSupport(properties.getP6spy());
        AbstractDataSourceWarp seataDataSourceWarp = new SeataDataSourceWarp();
        seataDataSourceWarp.setSupport(properties.getSeata());
        p6spyDataSourceWarp.setNextWarp(seataDataSourceWarp);
        return p6spyDataSourceWarp;
    }


    /**
     * 数据源构造，提供已配置的数据
     * <p>
     * 包括：1、数据源配置、创建 2、数据源包装
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DataSourceProvider dynamicDataSourceProvider() {
        return new YmlDataSourceProvider();
    }


    /**
     * 动态获取数据源
     * 配置所有支持的处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ChainHandler dynamicFetchDataSourceNameChainHandler() {
        AbstractDataSourceChainHandler requestHeaderDataSourceHandler = new RequestHeaderDataSourceHandler();
        AbstractDataSourceChainHandler sessionDataSourceHandler = new SessionDataSourceHandler();
        AbstractDataSourceChainHandler spELDataSourceHandler = new SpELDataSourceHandler();
        sessionDataSourceHandler.setNextHandler(spELDataSourceHandler);
        requestHeaderDataSourceHandler.setNextHandler(sessionDataSourceHandler);
        return requestHeaderDataSourceHandler;
    }

    /**
     * 配置AOP
     *
     * @param dynamicFetchDataSourceNameChainHandler
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DataSourceAnnotationAdvisor dataSourceAnnotationAdvisor(ChainHandler dynamicFetchDataSourceNameChainHandler) {
        DataSourceAnnotationInterceptor interceptor = new DataSourceAnnotationInterceptor();
        interceptor.setDynamicChainHandler(dynamicFetchDataSourceNameChainHandler);
        return new DataSourceAnnotationAdvisor(interceptor);
    }

}
