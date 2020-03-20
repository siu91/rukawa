package org.siu.rukawa.datasource.core.exception;

/**
 * 未支持的数据源类型
 *
 * @Author Siu
 * @Date 2020/3/19 14:51
 * @Version 0.0.1
 */
public class UnSupportDataSourceError extends DynamicDataSourceError {

    public UnSupportDataSourceError() {
        super(UnSupportDataSourceError.class.getSimpleName().toUpperCase());
    }

    public UnSupportDataSourceError(String msg) {
        super(UnSupportDataSourceError.class.getSimpleName().toUpperCase() + "-[" + msg + "]");
    }
}
