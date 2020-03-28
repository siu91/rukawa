package org.siu.rukawa.datasource.core.strategy;


import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡策略实现
 *
 * @Author Siu
 * @Date 2020/3/19 14:09
 * @Version 0.0.1
 */
public class LoadBalanceDataSourceLookupStrategy implements DataSourceLookupStrategy {

    private AtomicInteger balance = new AtomicInteger(0);


    @Override
    public DataSource lookup(List<DataSource> candidate) {
        return candidate.get(balance.addAndGet(1) % candidate.size());
    }
}
