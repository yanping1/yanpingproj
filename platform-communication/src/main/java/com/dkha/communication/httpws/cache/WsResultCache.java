package com.dkha.communication.httpws.cache;

import com.dkha.common.validate.UtilValidate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author Spring
 * @Since 2019/11/13 10:25
 * @Description 返回结果缓存
 */
@Component
//@PropertySource({"classpath:system.properties"})
public class WsResultCache {

    //@Value("${cache.time}")
    private static int cacheTime = 60 * 24;

    public static Cache<String, String> ws_result_cache = CacheBuilder.newBuilder().expireAfterAccess(cacheTime, TimeUnit.MINUTES).build();

    /**
     * put data to cache
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        ws_result_cache.put(key, value);
    }

    /**
     * get data from cache
     * @param key
     */
    public String get(String key) {
        String wsResult = ws_result_cache.getIfPresent(key);
        if (UtilValidate.isNotEmpty(wsResult)) {
            ws_result_cache.invalidate(key);
            return wsResult;
        }
        return null;
    }

}
