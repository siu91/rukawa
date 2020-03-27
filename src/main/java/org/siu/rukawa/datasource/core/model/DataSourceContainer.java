package org.siu.rukawa.datasource.core.model;

import com.p6spy.engine.spy.P6DataSource;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.siu.rukawa.datasource.core.exception.NotFoundPrimaryDataSourceError;
import org.siu.rukawa.datasource.core.strategy.Strategy;
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
     * 容器ID
     */
    @Getter
    private String id;

    /**
     * 主数据源
     */
    @Getter
    private String primary;

    private Strategy strategy;

    /**
     * 所有数据源
     */
    @Getter
    private Map<String, DataSource> dataSources;

    /**
     * 分组数据源
     * 分组标识 org.siu.myboot.dds.constant.Constant#UNDERLINE_SPLIT
     */
    @Getter
    private Map<String, DataSourceGroup> groups;


    public DataSourceContainer(String primary, Strategy strategy) {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.primary = primary;
        this.dataSources = new LinkedHashMap<>();
        this.groups = new ConcurrentHashMap<>();
    }


    /**
     * 查找数据源数据源
     *
     * @return
     */
    public DataSource lookupDataSource(String key) {
        log.debug("lookup the datasource [{}]", key);
        if (!StringUtils.hasText(key)) {
            return primary();
        }
        // 从分组中取
        if (!this.groups.isEmpty() && groups.containsKey(key)) {
            return groups.get(key).lookupDataSource(key);
        }
        // 从所有数据源取
        return this.dataSources.getOrDefault(key, primary());
    }

    /**
     * 主数据源
     *
     * @return
     */
    private DataSource primary() {
        log.debug("select the primary datasource");
        return this.getDataSources().get(this.primary);
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
        for (Map.Entry<String, DataSource> item : this.dataSources.entrySet()) {
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
            this.addGroup(dsd);
            log.info("load a datasource [{}] success", dsd.getKey());
        } else {
            log.warn("load a datasource [{}] failed, because it already exist", dsd.getKey());
        }
    }


    /**
     * 数据源分组
     *
     * @param dsd 数据源定义
     */
    private void addGroup(DataSourceDefinition dsd) {
        if (StringUtils.hasText(dsd.getGroup())) {
            if (groups.containsKey(dsd.getGroup())) {
                groups.get(dsd.getGroup()).add(dsd.getDataSource());
            } else {
                try {
                    DataSourceGroup datasourceGroup = new DataSourceGroup(dsd.getGroup(), this.strategy);
                    datasourceGroup.add(dsd.getDataSource());
                    groups.put(dsd.getGroup(), datasourceGroup);
                } catch (Exception e) {
                    log.error("add datasource [{}] error", dsd.getKey(), e);
                    dataSources.remove(dsd.getKey());
                }
            }
        }
    }


}
