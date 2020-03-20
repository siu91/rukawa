package org.siu.rukawa.datasource.core.model;

import lombok.Data;
import org.siu.rukawa.datasource.constant.Constant;

import javax.sql.DataSource;

/**
 * 定义的数据源对象
 *
 * @Author Siu
 * @Date 2020/3/18 23:31
 * @Version 0.0.1
 */
@Data
public class DataSourceDefinition {

    private String key;
    private String group;
    private DataSource dataSource;

    public DataSourceDefinition(String key, DataSource dataSource) {
        this.key = key;
        this.dataSource = dataSource;
        if (key.contains(Constant.UNDERLINE_SPLIT)) {
            this.group = key.split(Constant.UNDERLINE_SPLIT)[0];
        }
    }
}
