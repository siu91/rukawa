package org.siu.rukawa.datasource.core.provider.warp;

import javax.sql.DataSource;

/**
 * 数据源包装抽象
 *
 * @Author Siu
 * @Date 2020/3/19 16:02
 * @Version 0.0.1
 */
public abstract class AbstractDataSourceWarp implements DataSourceWarp {

    protected boolean support;
    private DataSourceWarp nextWarp;

    public void setNextWarp(DataSourceWarp nextWarp) {
        this.nextWarp = nextWarp;
    }

    @Override
    public DataSource warp(DataSource dataSource) {
        // 包装
        DataSource datasource = doWarp(dataSource);
        // 下一层包装
        if (nextWarp != null) {
            return nextWarp.warp(dataSource);
        }
        return datasource;
    }

    /**
     * 由实现类决定包装的数据源
     *
     * @return
     */
    public abstract DataSource doWarp(DataSource dataSource);


    public abstract void setSupport(boolean support);

}
