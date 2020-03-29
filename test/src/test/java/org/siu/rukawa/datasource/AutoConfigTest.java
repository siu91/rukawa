package org.siu.rukawa.datasource;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siu.rukawa.datasource.autoconfigure.properties.DataSourceProperty;
import org.siu.rukawa.datasource.core.event.AddDataSourceEvent;
import org.siu.rukawa.datasource.core.event.EventPublisher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("rukawa-test")
@SpringBootTest(classes = RukawaTestApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AutoConfigTest {

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
