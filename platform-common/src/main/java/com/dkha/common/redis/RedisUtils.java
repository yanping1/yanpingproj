

package com.dkha.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**  默认过期时长为1天，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24L;

    /** 长登录--保持15天 */
    public final static long LONG_LOGIN_EXPIRE = 60 * 60 * 24 * 15L;

    /** 短登录-保持30分钟 */
    public final static long SHORT_LOGIN_EXPIRE = 60 * 30L;

    /** 30分钟缓存有效期 */
    public final static long SHORT_DEFAULT_EXPIRE = 60 * 30L;

    /** 默认短信过期时间 */
    public final static long SHORT_MESSAGE_EXPIRE = 60 * 5L;

    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1L;

    public void set(String key, Object value, long expire){
        redisTemplate.opsForValue().set(key, value);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public Object get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
        return value;
    }


    public Object get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Map<String, Object> hGetAll(String key){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public void hMSet(String key, Map<String, Object> map){
        hMSet(key, map, DEFAULT_EXPIRE);
    }

    public void hMSet(String key, Map<String, Object> map, long expire){
        redisTemplate.opsForHash().putAll(key, map);

        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public void hSet(String key, String field, Object value) {
        hSet(key, field, value, DEFAULT_EXPIRE);
    }

    public void hSet(String key, String field, Object value, long expire) {
        redisTemplate.opsForHash().put(key, field, value);

        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public void expire(String key, long expire){
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public void expire(String key, long expire,TimeUnit timeUnit){
        redisTemplate.expire(key, expire, timeUnit);
    }


    public void hDel(String key, Object... fields){
        redisTemplate.opsForHash().delete(key, fields);
    }

    public void leftPush(String key, Object value){
        leftPush(key, value, DEFAULT_EXPIRE);
    }

    public void leftPush(String key, Object value, long expire){
        redisTemplate.opsForList().leftPush(key, value);

        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public Object rightPop(String key){
        return redisTemplate.opsForList().rightPop(key);
    }
}
