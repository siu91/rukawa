package org.siu.rukawa.datasource.core.provider.builder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.siu.rukawa.datasource.autoconfigure.properties.DataSourceProperty;
import org.siu.rukawa.datasource.autoconfigure.properties.DynamicDataSourceProperties;
import org.siu.rukawa.datasource.core.exception.DynamicDataSourceError;
import org.siu.rukawa.datasource.core.exception.UnSupportDataSourceError;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * 构建数据源
 *
 * @Author Siu
 * @Date 2020/3/18 21:37
 * @Version 0.0.1
 */
@Slf4j
public class DataSourceBuilder {

    @Getter
    private DynamicDataSourceProperties properties;

    public DataSourceBuilder(DynamicDataSourceProperties properties) {
        this.properties = properties;
    }

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
     * 默认使用 hikari
     *
     * @param dataSourceProperty
     * @return
     */
    public DataSource builderDataSource(DataSourceProperty dataSourceProperty) throws DynamicDataSourceError {
        DataSource dataSource;
        //如果是jndi数据源
        String jndiName = dataSourceProperty.getJndi();
        if (StringUtils.hasText(dataSourceProperty.getJndi())) {
            dataSource = jndiDataSourceBuilder.builder(dataSourceProperty);
        } else {
            Class<? extends DataSource> type = dataSourceProperty.getType();
            if (type == null) {
                if (hikariExists) {
                    dataSource = hikariDataSourceBuilder.builder(properties.getHikari(), dataSourceProperty);
                } else if (druidExists) {
                    dataSource = druidDataSourceBuilder.builder(dataSourceProperty);
                } else {
                    dataSource = baseDataSourceBuilder.builder(dataSourceProperty);
                }
            } else if (DRUID_DATASOURCE.equals(type.getName())) {
                dataSource = druidDataSourceBuilder.builder(dataSourceProperty);
            } else if (HIKARI_DATASOURCE.equals(type.getName())) {
                dataSource = hikariDataSourceBuilder.builder(properties.getHikari(), dataSourceProperty);
            } else {
                dataSource = baseDataSourceBuilder.builder(dataSourceProperty);
            }
        }
        return dataSource;
    }


    // region inner class

    static class BaseDataSourceBuilder {
        private static Method createMethod;
        private static Method typeMethod;
        private static Method urlMethod;
        private static Method usernameMethod;
        private static Method passwordMethod;
        private static Method driverClassNameMethod;
        private static Method buildMethod;

        static {
            //to support springboot 1.5 and 2.x
            Class<?> builderClass = null;
            try {
                builderClass = Class.forName("org.springframework.boot.jdbc.DataSourceBuilder");
            } catch (Exception ignored) {
            }
            if (builderClass == null) {
                try {
                    builderClass = Class.forName("org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder");
                } catch (Exception e) {
                    log.warn("not in springBoot ENV,could not create BasicDataSourceCreator");
                }
            }
            if (builderClass != null) {
                try {
                    createMethod = builderClass.getDeclaredMethod("create");
                    typeMethod = builderClass.getDeclaredMethod("type", Class.class);
                    urlMethod = builderClass.getDeclaredMethod("url", String.class);
                    usernameMethod = builderClass.getDeclaredMethod("username", String.class);
                    passwordMethod = builderClass.getDeclaredMethod("password", String.class);
                    driverClassNameMethod = builderClass.getDeclaredMethod("driverClassName", String.class);
                    buildMethod = builderClass.getDeclaredMethod("build");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public DataSource builder(DataSourceProperty dataSourceProperty) throws DynamicDataSourceError {
            try {
                Object o1 = createMethod.invoke(null);
                Object o2 = typeMethod.invoke(o1, dataSourceProperty.getType());
                Object o3 = urlMethod.invoke(o2, dataSourceProperty.getUrl());
                Object o4 = usernameMethod.invoke(o3, dataSourceProperty.getUsername());
                Object o5 = passwordMethod.invoke(o4, dataSourceProperty.getPassword());
                Object o6 = driverClassNameMethod.invoke(o5, dataSourceProperty.getDriverClassName());
                return (DataSource) buildMethod.invoke(o6);
            } catch (Exception e) {
                throw new DynamicDataSourceError(e.getMessage());
            }
        }

    }

    /**
     * Java Naming and Directory Interface
     * <p>
     * JNDI的作用：就是将资源引入到服务器中。可以将JNDI当成一个仓库。将Java对象放入到JNDI中去。
     * jdbc是java去找数据库驱动，jndi是通过你的服务器配置（如Tomcat）的配置文件context来找数据库驱动~
     * 现在JNDI已经成为J2EE的标准之一，所有的J2EE容器都必须提供一个JNDI的服务。
     * 为了数据库资源的管理，在容器中配置一个数据库连接池，使用JNDI 来管理
     * 这样容器中运行多个服务的时候，每个服务只需添加一个jndi的名称就可以连接到数据库了
     * 如果不使用jndi的方式，直接在项目中配置数据库连接池，那么每个项目需要配置一次，如果更改数据库地址时，
     * 每个项目的数据库连接方式都要更改，比较麻烦，使用jndi的话，直接更改一下jndi里面的数据库连接池的配置就可以了，方便一些。
     */
    static class JndiDataSourceBuilder {
        private static final JndiDataSourceLookup JNDI_DATA_SOURCE_LOOKUP = new JndiDataSourceLookup();

        public DataSource builder(DataSourceProperty dataSourceProperty) {
            return JNDI_DATA_SOURCE_LOOKUP.getDataSource(dataSourceProperty.getJndi());
        }
    }

    static class HikariDataSourceBuilder {

        public DataSource builder(HikariConfig hikariConfig, DataSourceProperty dataSourceProperty) {
            hikariConfig.setUsername(dataSourceProperty.getUsername());
            hikariConfig.setPassword(dataSourceProperty.getPassword());
            hikariConfig.setJdbcUrl(dataSourceProperty.getUrl());
            hikariConfig.setDriverClassName(dataSourceProperty.getDriverClassName());
            return new HikariDataSource(hikariConfig);
        }
    }

    /**
     * TODO 支持durid
     */
    static class DruidDataSourceBuilder {
        public DataSource builder(DataSourceProperty dataSourceProperty) throws UnSupportDataSourceError {
            throw new UnSupportDataSourceError();
        }
    }

    // endregion

}
