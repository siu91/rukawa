package org.siu.rukawa.datasource.support;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.time.LocalDateTime;

/**
 * 自定义p6spy日志格式
 *
 * @Author Siu
 * @Date 2020/2/22 16:46
 * @Version 0.0.1
 */
public class P6SpyMessageFormat implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return
                "[ " + LocalDateTime.now() + " ] [ took " + elapsed + "ms | " + category.toUpperCase() + " | connection " + connectionId + "]"
                         + "\n" + url // 不打印url配置
                        + (!"".equals(sql.trim()) ? "\n" + sql + ";" : "");
    }
}
