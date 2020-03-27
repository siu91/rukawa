package org.siu.rukawa.datasource.core.event;

import org.siu.rukawa.datasource.autoconfigure.properties.DataSourceProperty;
import org.springframework.context.ApplicationEvent;

/**
 * 添加数据源事件
 * <p>
 * 用与动态新增数据源
 * SpringContextHolder.publishEvent(AddDataSourceEvent)
 *
 * @Author Siu
 * @Date 2020/3/26 21:46
 * @Version 0.0.1
 */
public class AddDataSourceEvent extends ApplicationEvent {

    private String dsName;

    public String getDsName() {
        return dsName;
    }

    /**
     * 数据源配置
     *
     * @param dsName
     * @param property
     */
    public AddDataSourceEvent(String dsName, DataSourceProperty property) {
        super(property);
        this.dsName = dsName;
    }
}
