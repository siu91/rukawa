package org.siu.rukawa.datasource.autoconfigure.properties;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.siu.rukawa.datasource.core.exception.NotFoundPrimaryDataSourceError;
import org.siu.rukawa.datasource.core.strategy.DataSourceSelectionStrategy;
import org.siu.rukawa.datasource.core.strategy.LoadBalanceDataSourceSelectionStrategy;
import org.siu.rukawa.datasource.support.P6SpyMessageFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自动配置的所有属性
 *
 * @Author Siu
 * @Date 2020/3/18 22:04
 * @Version 0.0.1
 */
@Slf4j
@Data
public class DynamicDataSourceProperties implements InitializingBean {
    public static final String PREFIX = "spring.datasource.dynamic";

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.hasText(primary)) {
            // 未设置主数据源，默认设置第一个数据源为主数据源
            this.primary = datasourceMap.keySet().toArray(new String[this.datasourceMap.size()])[0];
            log.info("未设置主数据源，设置第一个数据源为主数据源-[{}]", this.primary);
        } else {
            // 判断设置的主数据源是否在配置中
            if (!datasourceMap.containsKey(this.primary)) {
                log.error("已配置的数据源{},未找到配置的主数据源-[{}],请检查配置", datasourceMap.keySet(), this.primary);
                throw new NotFoundPrimaryDataSourceError(this.primary);
            }
        }
    }

    /**
     * 未设置主数据源，默认设置第一个数据源为主数据源
     */
    private String primary;

    /**
     * 是否使用p6spy监控SQL，默认开启
     */
    private Boolean p6spy = true;

    /**
     * p6spy 日志输出格式,默认使用 P6SpyMessageFormat
     */
    private Class<? extends MessageFormattingStrategy> p6spyMessageFormat = P6SpyMessageFormat.class;

    /**
     * 是否启用分布式事务seata,默认不使用
     */
    private Boolean seata = false;

    /**
     * 配置所有数据源<name,property>：默认第一个是主数据源，如master、slave0_0、slave_0_1,下划线区分分组
     */
    private Map<String, DataSourceProperty> datasourceMap = new LinkedHashMap<>();
    /**
     * 分组数据源时，选择算法,默认一组数据源内使用负载均衡算法
     */
    private Class<? extends DataSourceSelectionStrategy> strategy = LoadBalanceDataSourceSelectionStrategy.class;
    /**
     * 切面优先级
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;

    /**
     * HikariCp全局参数配置
     */
    @NestedConfigurationProperty
    private HikariConfig hikari = new HikariConfig();


}
