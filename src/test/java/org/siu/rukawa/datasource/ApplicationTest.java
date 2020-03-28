package org.siu.rukawa.datasource;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siu.rukawa.datasource.autoconfigure.properties.DataSourceProperty;
import org.siu.rukawa.datasource.autoconfigure.properties.DynamicDataSourceProperties;
import org.siu.rukawa.datasource.core.DynamicRoutingDataSource;
import org.siu.rukawa.datasource.core.event.AddDataSourceEvent;
import org.siu.rukawa.datasource.core.event.EventPublisher;
import org.siu.rukawa.datasource.core.provider.DataSourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@ActiveProfiles("rukawa-test-hfashjfsdkljfkl")
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationTest {

    @Autowired
    private WebApplicationContext wac;


    @Resource
    EventPublisher eventPublisher;




    @Test
    public void test() {
        DataSourceProperty property = new DataSourceProperty();
        property.setDriverClassName("org.h2.Driver");
        property.setUrl("jdbc:h2:mem:test");
        property.setUsername("sa");
        property.setPassword("");

        AddDataSourceEvent event = new AddDataSourceEvent("tets111", property);

        eventPublisher.publishEvent(event);
        System.out.println("-------------");
    }

}
