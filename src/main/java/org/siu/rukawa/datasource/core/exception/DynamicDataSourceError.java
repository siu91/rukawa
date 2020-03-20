package org.siu.rukawa.datasource.core.exception;

/**
 * 异常
 *
 * @Author Siu
 * @Date 2020/3/19 14:48
 * @Version 0.0.1
 */
public class DynamicDataSourceError extends Exception {

    public DynamicDataSourceError(String message) {
        super("Dynamic DataSource Error[" + message + "]");
    }
}
