package org.siu.rukawa.datasource.autoconfigure.properties;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.sql.DataSource;

/**
 * 数据源配置属性
 *
 * @Author Siu
 * @Date 2020/3/18 22:04
 * @Version 0.0.1
 */
@Data
@Accessors(chain = true)
public class DataSourceProperty {

    /**
     * 非空时即启用
     */
    private String jndi;

    /**
     * 连接池类型
     * 如 Druid、HikariCp
     */
    private Class<? extends DataSource> type;


    /**
     * driver
     */
    private String driverClassName;

    /**
     * url
     */
    private String url;

    /**
     * username
     */
    private String username;

    /**
     * Jpassword
     */
    private String password;
}
