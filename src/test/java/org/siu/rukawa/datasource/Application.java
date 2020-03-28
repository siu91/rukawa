package org.siu.rukawa.datasource;

import org.siu.rukawa.datasource.autoconfigure.properties.DynamicDataSourceProperties;
import org.siu.rukawa.datasource.core.DynamicRoutingDataSource;
import org.siu.rukawa.datasource.core.provider.DataSourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }


  @Autowired
  DynamicDataSourceProperties properties;

  @Bean
  public DataSource dataSource(DataSourceProvider provider) {
    DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
    dynamicRoutingDataSource.setPrimary(this.properties.getPrimary());
    dynamicRoutingDataSource.setProvider(provider);
    dynamicRoutingDataSource.setStrategy(this.properties.getStrategy());
    return dynamicRoutingDataSource;
  }
}