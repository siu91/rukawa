package org.siu.rukawa.datasource.core.provider.builder;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.siu.rukawa.datasource.autoconfigure.properties.DataSourceProperty;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * 构建数据源
 *
 * @Author Siu
 * @Date 2020/3/18 21:37
 * @Version 0.0.1
 */
@Slf4j
@NoArgsConstructor
public class DataSourceBuilder {

    /**
     * DRUID数据源类
     */
    static String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";
    /**
     * HikariCp数据源
     */
    static String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

    /**
     * 是否存在druid
     */
    private static Boolean druidExists = false;
    /**
     * 是否存在hikari
     */
    private static Boolean hikariExists = false;

    static {
        try {
            Class.forName(DRUID_DATASOURCE);
            druidExists = true;
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName(HIKARI_DATASOURCE);
            hikariExists = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    private BaseDataSourceBuilder baseDataSourceBuilder = new BaseDataSourceBuilder();
    private JndiDataSourceBuilder jndiDataSourceBuilder = new JndiDataSourceBuilder();
    private HikariDataSourceBuilder hikariDataSourceBuilder = new HikariDataSourceBuilder();
    private DruidDataSourceBuilder druidDataSourceBuilder = new DruidDataSourceBuilder();

    /**
     * 根据数据源配置，构建相应数据源
     *
     * @param dataSourceProperty
     * @return
     */
    public DataSource builderDataSource(DataSourceProperty dataSourceProperty) {
        DataSource dataSource;
        //如果是jndi数据源
        String jndiName = dataSourceProperty.getJndi();
        if (StringUtils.hasText(dataSourceProperty.getJndi())) {
            dataSource = jndiDataSourceBuilder.builder(dataSourceProperty);
        } else {
            Class<? extends DataSource> type = dataSourceProperty.getType();
            if (type == null) {
                if (druidExists) {
                    dataSource = druidDataSourceBuilder.builder(dataSourceProperty);
                } else if (hikariExists) {
                    dataSource = hikariDataSourceBuilder.builder(dataSourceProperty);
                } else {
                    dataSource = baseDataSourceBuilder.builder(dataSourceProperty);
                }
            } else if (DRUID_DATASOURCE.equals(type.getName())) {
                dataSource = druidDataSourceBuilder.builder(dataSourceProperty);
            } else if (HIKARI_DATASOURCE.equals(type.getName())) {
                dataSource = hikariDataSourceBuilder.builder(dataSourceProperty);
            } else {
                dataSource = baseDataSourceBuilder.builder(dataSourceProperty);
            }
        }
        return dataSource;
    }


    // region inner class TODO 实现构建数据源的builder方法

    static class BaseDataSourceBuilder {
        public DataSource builder(DataSourceProperty dataSourceProperty) {
            return null;
        }

    }

    static class JndiDataSourceBuilder {
        public DataSource builder(DataSourceProperty dataSourceProperty) {
            return null;
        }
    }

    static class HikariDataSourceBuilder {
        public DataSource builder(DataSourceProperty dataSourceProperty) {
            return null;
        }
    }

    static class DruidDataSourceBuilder {
        public DataSource builder(DataSourceProperty dataSourceProperty) {
            return null;
        }
    }

    // endregion

}
