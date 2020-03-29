package org.siu.rukawa.datasource.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.util.concurrent.TimeUnit;

/**
 * 自定义切点：缓存已经匹配的方法
 *
 * @Author Siu
 * @Date 2020/3/29 15:01
 * @Version 0.0.1
 */
public class MatcherCache {

    public MatcherCache() throws Exception {
        CACHE_LOADER.load("");
    }

    protected static final CacheLoader<String, String> CACHE_LOADER = new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            // 可以从本地文件、数据库等读取加载
            return null;
        }
    };

    protected static final Cache<String, String> CACHE = CacheBuilder.newBuilder()
            //设置cache的初始大小
            .initialCapacity(10)
            // 缓存的最大大小
            .maximumSize(2000)
            // 缓存的最大重量 注:此功能不能与@link maximumsize结合使用。
           // .maximumWeight(2000)
            //设置并发数为n，即同一时间最多只能有n个线程往cache执行写入操作
            .concurrencyLevel(16)
            //设置cache中的数据在写入之后的存活时间为7天
            .expireAfterWrite(7, TimeUnit.DAYS)
            //设置缓存多久没读就自动清除
            .expireAfterAccess(2, TimeUnit.DAYS)
            //构建cache实例
            .build(CACHE_LOADER);


    /**
     * 获取缓存中的值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return CACHE.getIfPresent(key);
    }


    /**
     * 放入缓存
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        CACHE.put(key, value);
    }

}
