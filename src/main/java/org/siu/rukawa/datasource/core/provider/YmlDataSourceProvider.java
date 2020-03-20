package org.siu.rukawa.datasource.core.provider;

import lombok.AllArgsConstructor;
import org.siu.rukawa.datasource.autoconfigure.properties.DataSourceProperty;
import org.siu.rukawa.datasource.core.exception.DynamicDataSourceError;
import org.siu.rukawa.datasource.core.model.DataSourceDefinition;

import java.util.List;
import java.util.Map;

/**
 * DataSource Provider 实现类
 *
 * @Author Siu
 * @Date 2020/3/18 23:15
 * @Version 0.0.1
 */
@AllArgsConstructor
public class YmlDataSourceProvider extends AbstractDataSourceProvider implements DataSourceProvider {



    @Override
    public List<DataSourceDefinition> buildDataSources() throws DynamicDataSourceError {
        return this.doBuildDataSources();
    }

}
