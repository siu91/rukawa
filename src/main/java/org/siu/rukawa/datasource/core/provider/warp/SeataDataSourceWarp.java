package org.siu.rukawa.datasource.core.provider.warp;

import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * seata 支持
 *
 * @Author Siu
 * @Date 2020/3/19 16:14
 * @Version 0.0.1
 */
@Slf4j
public class SeataDataSourceWarp extends AbstractDataSourceWarp implements DataSourceWarp {


    @Override
    public DataSource doWarp(DataSource dataSource) {
        if (this.support) {
            dataSource = new DataSourceProxy(dataSource);
        }
        return dataSource;
    }

    /**
     * 判断依赖
     *
     * @param seata
     */
    @Override
    public void setSupport(boolean seata) {
        if (seata) {
            try {
                Class.forName("io.seata.rm.datasource.DataSourceProxy");
                log.info("开启seata支持");
                this.support = true;
            } catch (Exception e) {
                log.info("无法开启seata支持，未找到seata");
            }
        } else {
            this.support = false;
        }
    }


}
