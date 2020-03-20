package org.siu.rukawa.datasource.core.provider.warp;

import com.p6spy.engine.spy.P6DataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * P6spy 支持
 *
 * @Author Siu
 * @Date 2020/3/19 16:14
 * @Version 0.0.1
 */
@Slf4j
public class P6spyDataSourceWarp extends AbstractDataSourceWarp implements DataSourceWarp {


    @Override
    public DataSource doWarp(DataSource dataSource) {
        if (this.support) {
            dataSource = new P6DataSource(dataSource);
        }
        return dataSource;
    }

    /**
     * 判断依赖
     *
     * @param p6spy
     */
    @Override
    public void setSupport(boolean p6spy) {
        if (p6spy) {
            try {
                Class.forName("com.p6spy.engine.spy.P6DataSource");
                log.info("开启p6spy支持");
                this.support = true;
            } catch (Exception e) {
                log.info("无法开启p6spy支持，未找到p6spy插件");
            }
        } else {
            this.support = false;
        }
    }


}
