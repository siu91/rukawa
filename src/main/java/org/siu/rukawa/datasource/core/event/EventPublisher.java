package org.siu.rukawa.datasource.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

/**
 * 事件发布
 *
 * @Author Siu
 * @Date 2020/3/27 9:15
 * @Version 0.0.1
 */
@Slf4j
public class EventPublisher implements ApplicationContextAware {

    private ApplicationContext applicationContext = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * 清除ApplicationContext为Null.
     */
    public void clear() {
        if (log.isDebugEnabled()) {
            log.debug("清除ApplicationContext:" + applicationContext);
        }
        applicationContext = null;
    }

    /**
     * 发布事件
     *
     * @param event
     */
    public void publishEvent(ApplicationEvent event) {
        if (applicationContext == null) {
            return;
        }
        applicationContext.publishEvent(event);
    }
}
