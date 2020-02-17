package com.dkha.communication.httpws.cache;

import com.dkha.communication.httpws.common.WYRequestResponse;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;


import javax.servlet.ServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @Description: 缓存请求应答的Response对象，以供后期解析协议后数据的返回
 * @Title:
 * @Package com.dkha.communication.httpws.cache
 * @author: huangyugang
 * @date: 2019/11/29 14:03
 * @Copyright: 成都电科慧安
 */
@Component
public class ResponseCache {

    /**最大缓存3分钟*/
    public static Cache<String, WYRequestResponse> responsemap = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build();
    /**设置缓存信息*/
    public void set(String key, WYRequestResponse value) {
        responsemap.put(key, value);
    }
    /**获取缓存信息*/
    public WYRequestResponse get(String key) {
        return  responsemap.getIfPresent(key);
    }

}
