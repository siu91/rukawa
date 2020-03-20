package org.siu.rukawa.datasource.core;

import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author Siu
 * @Date 2020/3/18 22:40
 * @Version 0.0.1
 */
public abstract class AbstractRoutingDataSource extends AbstractDataSource {

    /**
     * 子类实现查找最终的数据源
     *
     * @return 数据源
     */
    protected abstract DataSource lookupDataSource();

    @Override
    public Connection getConnection() throws SQLException {
        return lookupDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return lookupDataSource().getConnection(username, password);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return lookupDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return (iface.isInstance(this) || lookupDataSource().isWrapperFor(iface));
    }
}
