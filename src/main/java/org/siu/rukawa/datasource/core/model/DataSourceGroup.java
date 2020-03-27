package org.siu.rukawa.datasource.core.model;

import lombok.Getter;
import org.siu.rukawa.datasource.core.strategy.DataSourceSelectionStrategy;
import org.siu.rukawa.datasource.core.strategy.LoadBalanceDataSourceSelectionStrategy;
import org.siu.rukawa.datasource.core.strategy.RandomDataSourceSelectionStrategy;
import org.siu.rukawa.datasource.autoconfigure.properties.Strategy;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 分组数据源
 *
 * @Author Siu
 * @Date 2020/3/18 16:53
 * @Version 0.0.1
 */
public class DataSourceGroup {

    @Getter
    private String name;

    @Getter
    private List<DataSource> dataSources;

    @Getter
    private DataSourceSelectionStrategy strategy;

    public DataSourceGroup(String name) {
        this.name = name;
        this.dataSources = new ArrayList<>();
        this.strategy = new LoadBalanceDataSourceSelectionStrategy();
    }

    public DataSourceGroup(String name, DataSourceSelectionStrategy strategy) {
        this.name = name;
        this.dataSources = new ArrayList<>();
        this.strategy = strategy;
    }

    public DataSourceGroup(String name, Strategy strategy) {
        this.name = name;
        this.dataSources = new ArrayList<>();
        this.strategy = Strategy.RANDOM == strategy ? new RandomDataSourceSelectionStrategy() : new LoadBalanceDataSourceSelectionStrategy();
    }

    /**
     * 添加数据源
     *
     * @param ds
     */
    public void add(DataSource ds) {
        this.dataSources.add(ds);
    }

    /**
     * 查找数据源
     *
     * @param key
     * @return
     */
    public DataSource lookupDataSource(String key) {
        return strategy.lookup(this.dataSources);
    }


}
