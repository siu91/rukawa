package org.siu.rukawa.datasource.core.model;

import com.google.common.collect.ArrayListMultimap;
import com.p6spy.engine.spy.P6DataSource;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.siu.rukawa.datasource.core.exception.NotFoundPrimaryDataSourceError;
import org.siu.rukawa.datasource.autoconfigure.properties.Strategy;
import org.siu.rukawa.datasource.core.strategy.DataSourceLookupStrategy;
import org.siu.rukawa.datasource.core.strategy.LoadBalanceDataSourceLookupStrategy;
import org.siu.rukawa.datasource.core.strategy.RandomDataSourceLookupStrategy;
import org.springframework.util.StringUtils;

/**
 * 数据源容器
 *
 * @Author Siu
 * @Date 2020/3/18 16:46
 * @Version 0.0.1
 */
@Slf4j
@Accessors(chain = true)
public class DataSourceContainer {

    /**
     * 主数据源
     */
    @Getter
    private String primary;

    /**
     * 数据源查找策略
     */
    private DataSourceLookupStrategy strategy;

    /**
     * 所有数据源
     */
    @Getter
    private ArrayListMultimap<String, DataSource> dataSources;


    public DataSourceContainer(String primary, Strategy strategy) {
        this.primary = primary;
        this.strategy = Strategy.RANDOM == strategy ? new RandomDataSourceLookupStrategy() : new LoadBalanceDataSourceLookupStrategy();
        this.dataSources = ArrayListMultimap.create();
    }


    /**
     * 查找数据源数据源
     *
     * @return
     */
    public DataSource lookup(String key) {
        log.debug("lookup the datasource [{}]", key);
        if (!StringUtils.hasText(key)) {
            return primary();
        }

        // 从所有数据源取
        DataSource ds = this.strategy.lookup(this.dataSources.get(key));
        if (ds == null) {
            ds = this.primary();
        }
        return ds;
    }


    /**
     * 主数据源
     *
     * @return
     */
    private DataSource primary() {
        log.debug("select the primary datasource");
        return this.getDataSources().get(this.primary).get(0);
    }


    /**
     * 加载数据源
     *
     * @param dataSourceDefinitionList
     */
    public void load(List<DataSourceDefinition> dataSourceDefinitionList) {
        dataSourceDefinitionList.forEach(this::add);

    }


    /**
     * 刷新数据源：数据源配置完成后刷新
     */
    public void flush() throws NotFoundPrimaryDataSourceError {
        // 检测默认数据源设置
        if (dataSources.containsKey(primary)) {
            log.info("已初始化加载[{}]个datasource,primary datasource-[{}]", dataSources.size(), primary);
        } else {
            throw new NotFoundPrimaryDataSourceError();
        }
    }


    /**
     * 关闭数据源
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void destroy() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        for (Map.Entry<String, DataSource> item : this.dataSources.entries()) {
            String name = item.getKey();
            DataSource dataSource = item.getValue();
            if (dataSource instanceof DataSourceProxy) {
                dataSource = ((DataSourceProxy) dataSource).getTargetDataSource();
            }
            if (dataSource instanceof P6DataSource) {
                Field realDataSourceField = P6DataSource.class.getDeclaredField("realDataSource");
                realDataSourceField.setAccessible(true);
                dataSource = (DataSource) realDataSourceField.get(dataSource);
            }

            Class<? extends DataSource> clazz = dataSource.getClass();
            try {
                Method closeMethod = clazz.getDeclaredMethod("close");
                closeMethod.invoke(dataSource);
            } catch (NoSuchMethodException e) {
                log.warn("close datasource [{}] failed,", name);
            }
        }

    }

    /**
     * 添加数据源
     *
     * @param dsd 数据源定义
     */
    public synchronized void add(DataSourceDefinition dsd) {
        if (!dataSources.containsKey(dsd.getKey())) {
            dataSources.put(dsd.getKey(), dsd.getDataSource());
            // 分组数据源
            if (StringUtils.hasText(dsd.getGroup())) {
                dataSources.put(dsd.getGroup(), dsd.getDataSource());
            }
            log.info("load a datasource [{}] success", dsd.getKey());
        } else {
            log.warn("load a datasource [{}] failed, because it already exist", dsd.getKey());
        }
    }


}
