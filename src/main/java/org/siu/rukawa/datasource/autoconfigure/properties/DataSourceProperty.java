package org.siu.rukawa.datasource.autoconfigure.properties;

import lombok.Data;

import javax.sql.DataSource;

/**
 * 数据源配置属性
 *
 * @Author Siu
 * @Date 2020/3/18 22:04
 * @Version 0.0.1
 */
@Data
public class DataSourceProperty {

    private String jndi;

    /**
     * 连接池类型
     * 如 Druid、HikariCp
     */
    private Class<? extends DataSource> type;
}
