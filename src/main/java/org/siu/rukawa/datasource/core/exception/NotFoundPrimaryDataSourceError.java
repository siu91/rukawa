package org.siu.rukawa.datasource.core.exception;

/**
 * 未找到主数据源异常
 *
 * @Author Siu
 * @Date 2020/3/19 14:51
 * @Version 0.0.1
 */
public class NotFoundPrimaryDataSourceError extends DynamicDataSourceError {

    public NotFoundPrimaryDataSourceError() {
        super(NotFoundPrimaryDataSourceError.class.getSimpleName().toUpperCase());
    }

    public NotFoundPrimaryDataSourceError(String msg) {
        super(NotFoundPrimaryDataSourceError.class.getSimpleName().toUpperCase() + "-[" + msg + "]");
    }
}
