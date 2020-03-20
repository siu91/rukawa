package org.siu.rukawa.datasource.core.provider.warp;

import javax.sql.DataSource;

/**
 * 包装转化
 *
 * @Author Siu
 * @Date 2020/3/19 16:02
 * @Version 0.0.1
 */
public interface DataSourceWarp {

    /**
     * 包装数据源
     *
     * @param dataSource
     * @return
     */
    DataSource warp(DataSource dataSource);
}
