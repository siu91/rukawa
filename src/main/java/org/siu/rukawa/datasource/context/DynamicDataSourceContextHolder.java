package org.siu.rukawa.datasource.context;

import lombok.experimental.UtilityClass;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.util.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 数据源切换
 * <p>
 * 基于ThreadLocal
 * NamedInheritableThreadLocal（可以被子线程继承的）
 *
 * @Author Siu
 * @Date 2020/3/18 22:56
 * @Version 0.0.1
 */
@UtilityClass
public class DynamicDataSourceContextHolder {

    /**
     * NamedInheritableThreadLocal（可以被子线程继承的）
     * FILO 先进后出
     */
    private static final ThreadLocal<Deque<String>> CURRENT_DATASOURCE_KEY_HOLDER = new NamedInheritableThreadLocal<Deque<String>>("DynamicDataSourceContextHolder") {
        @Override
        protected Deque<String> initialValue() {
            return new ArrayDeque<>();
        }
    };

    /**
     * 获取当前线程数据源
     *
     * @return 数据源名称
     */
    public static String peek() {
        return CURRENT_DATASOURCE_KEY_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     *
     * @param key 数据源名称
     */
    public static void push(String key) {
        CURRENT_DATASOURCE_KEY_HOLDER.get().push(StringUtils.isEmpty(key) ? "" : key);
    }

    /**
     * 清空当前线程数据源
     */
    public static void poll() {
        Deque<String> deque = CURRENT_DATASOURCE_KEY_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            CURRENT_DATASOURCE_KEY_HOLDER.remove();
        }
    }


    public static void clear() {
        CURRENT_DATASOURCE_KEY_HOLDER.remove();
    }

}
