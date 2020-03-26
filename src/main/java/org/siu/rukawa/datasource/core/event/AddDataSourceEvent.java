package org.siu.rukawa.datasource.core.event;

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

    /**
     *
     * @param dsJsonText json 数据源配置
     */
    public AddDataSourceEvent(String dsJsonText) {
        super(dsJsonText);
    }
}
