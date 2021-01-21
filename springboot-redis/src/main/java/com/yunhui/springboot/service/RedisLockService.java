package com.yunhui.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-05-22 13:22
 */
@Service
public class RedisLockService {

    private static final String LOCK_SUCCESS = "OK";

    /**
     * 只在键不存在时，才对键进行设置操作
     */
    private static final String SET_IF_NOT_EXIST = "NX";
    /**
     * 设置键的过期时间 单位秒
     */
    private static final String SET_WITH_EXPIRE_TIME = "EX";


    @Autowired
    private Pool<Jedis> pool;


    public Pool<Jedis> getPool() {
        return pool;
    }

    /**
     * redis分布式锁
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public String lock(final String key, String value, long seconds) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            String result = jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, seconds);

            return result;

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
