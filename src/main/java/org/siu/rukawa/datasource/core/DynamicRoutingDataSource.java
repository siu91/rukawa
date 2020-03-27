package org.siu.rukawa.datasource.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.siu.rukawa.datasource.autoconfigure.properties.DataSourceProperty;
import org.siu.rukawa.datasource.context.DynamicDataSourceContextHolder;
import org.siu.rukawa.datasource.core.event.AddDataSourceEvent;
import org.siu.rukawa.datasource.core.exception.DynamicDataSourceError;
import org.siu.rukawa.datasource.core.exception.NotFoundPrimaryDataSourceError;
import org.siu.rukawa.datasource.core.model.DataSourceContainer;
import org.siu.rukawa.datasource.core.model.DataSourceDefinition;
import org.siu.rukawa.datasource.core.provider.DataSourceProvider;
import org.siu.rukawa.datasource.core.strategy.Strategy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;

import javax.sql.DataSource;
import java.util.List;

/**
 * 动态数据源实现类
 *
 * @Author Siu
 * @Date 2020/3/18 22:45
 * @Version 0.0.1
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean, ApplicationListener<AddDataSourceEvent> {

    @Setter
    private DataSourceProvider provider;

    /**
     * 数据源容器
     */
    private DataSourceContainer dataSourceContainer;

    /**
     * 主数据源标识
     */
    @Setter
    private String primary;

    @Setter
    private Strategy strategy;


    @Override
    public void afterPropertiesSet() throws DynamicDataSourceError {
        // 创建数据源
        List<DataSourceDefinition> dataSources = this.provider.buildDataSources();
        // 加载数据源到容器中
        this.dataSourceContainer = new DataSourceContainer(this.primary, this.strategy);
        this.dataSourceContainer.load(dataSources);
        // 刷新容器中数据源
        this.dataSourceContainer.flush();

    }


    @Override
    protected DataSource lookupDataSource() {
        String key = DynamicDataSourceContextHolder.peek();
        return dataSourceContainer.lookupDataSource(key);
    }

    @Override
    public void destroy() throws Exception {
        this.dataSourceContainer.destroy();
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(AddDataSourceEvent addDataSourceEvent) {
        log.info("动态添加数据源：{}", addDataSourceEvent.getDsName());
        DataSourceProperty property = (DataSourceProperty) addDataSourceEvent.getSource();
        DataSourceDefinition dataSourceDefinition = this.provider.buildOne(addDataSourceEvent.getDsName(), property);
        // TODO 判断是否重复的数据源配置，按照什么策略处理
        this.dataSourceContainer.add(dataSourceDefinition);
    }

   /* @Async
    @Order
    @EventListener(ApplicationEvent.class)
    public void addDataSourceEventListener(ApplicationEvent event) {
        log.info("动态添加数据源：{}", event.getSource());
    }*/

}
