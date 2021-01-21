package com.yunhui.springboot.service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.*;


@Service
public class RedisService {


    @Autowired
    private Pool<Jedis> pool;


    public Pool<Jedis> getPool() {
        return pool;
    }


    /**
     * 判断指定key是否存在
     *
     * @param key key
     * @return 如果存在，返回true，否则返回false
     */
    public boolean exists(final String key) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            return jedis.exists(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 删除keys
     *
     * @param keys
     * @return
     */
    public long delete(final String... keys) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            return jedis.del(keys);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 重命名key
     *
     * @param oldkey
     * @param newkey
     * @return
     */
    public String rename(final String oldkey, final String newkey) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            return jedis.rename(oldkey, newkey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置key的过期时间
     *
     * @param key
     * @param seconds
     * @return
     */
    public long expire(final String key, final int seconds) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            return jedis.expire(key, seconds);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 将keys插入到列表key的表尾 并且仅当key存在并且是一个列表
     * 当key不存在 rpushx 什么也不做
     *
     * @param key
     * @param keys
     * @return 返回的是列表的长度
     */
    public long rpushxTolist(final String key, String... keys) {
        Jedis jedis = null;

        try {
            jedis = getPool().getResource();

            return jedis.rpushx(key, keys);

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }


    /**
     * 将keys插入到列表key的表尾 如果key不存在，会创建一个空列表然后再执行rpush
     * 当key不是一个列表类型时，返回一个错误
     *
     * @param key
     * @param keys
     * @return
     */
    public long rpushTolist(final String key, String... keys) {
        Jedis jedis = null;

        try {
            jedis = getPool().getResource();

            return jedis.rpush(key, keys);

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
     * 当 key 不是集合类型时，返回一个错误。
     *
     * @param key
     * @param keys
     * @return
     */
    public long saddToSet(final String key, String... keys) {
        Jedis jedis = null;

        try {
            jedis = getPool().getResource();

            return jedis.sadd(key, keys);

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }


    /**
     * 插入或更新一个 value 到列表中，并指定index
     *
     * @param key
     * @param value
     * @param target
     * @return
     */
    public long insertOrUpdateList(final String key, String value, int target) {

        Jedis jedis = null;

        try {
            jedis = getPool().getResource();

            //查询value是否在list里
            List<String> resultants = jedis.lrange(key, 0, -1);


            if (CollectionUtils.isEmpty(resultants)) {
                //空list
                return jedis.rpush(key, value);

            }
            //找到value在list中的下标
            int index = resultants.indexOf(value);

            if (index == -1) {
                //没有找到下标，等于创建或插入
                try {
                    resultants.add(target, value);
                } catch (IndexOutOfBoundsException e) {
                    //越界修改失败
                    return 0;
                }
            } else {
                //调整位置
                try {

                    //删除原有位置上的element,采用linkedlist
                    List<String> templist = new LinkedList<String>(resultants);
                    templist.remove(index);
                    templist.add(target, value);
                    resultants = templist;

                } catch (IndexOutOfBoundsException e) {
                    //越界修改失败
                    return 0;
                }
            }
            //开始事务
            Transaction t = jedis.multi();
            //重新设置进去
            t.del(key);
            t.rpush(key, resultants.toArray(new String[resultants.size()]));
            List execs = t.exec();

            return execs.size();

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }


    }

    /**
     * 获取key对应的对象
     *
     * @param clazz
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getJSONToObject(Class<?> clazz, String key) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            String value = jedis.get(key);
            return (T) JSON.toJavaObject((JSON) JSON.parse(value), clazz);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取key对应的值
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 批量查询keys对应的对象
     *
     * @param clazz
     * @param keys
     * @param <T>
     * @return
     */
    public <T> List<T> mget(Class<?> clazz, final String... keys) {
        List<T> list = new ArrayList<T>();
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            List<String> values = jedis.mget(keys);

            for (String value : values) {
                list.add((T) JSON.toJavaObject((JSON) JSON.parse(value), clazz));
            }
            return list;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将对象设置为key对应的value
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> String setObjectToJSON(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            String str = JSON.toJSONString(value);
            return jedis.set(key, str);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置key-value
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            return jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回列表 key 中指定区间内的元素 0代表第一个元素 -1代表最后一个元素 -2代表倒数第二个元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            return jedis.lrange(key, start, end);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置key-value 并指定过期时间
     *
     * @param key
     * @param seconds
     * @param value
     * @param <T>
     * @return
     */
    public <T> String setex(final String key, final int seconds, final T value) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            String str = JSON.toJSONString(value);
            return jedis.setex(key, seconds, str);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 同时设置一个或多个 key-value键值对
     * 如果某个给定 key 已经存在，那么 MSET 会用新值覆盖原来的旧值
     *
     * @param map
     * @param <T>
     * @return
     */
    public <T> String mset(final Map<String, T> map) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            String[] keyValues = new String[map.size() * 2];
            int i = 0;
            for (String key : map.keySet()) {
                keyValues[2 * i] = key;
                keyValues[2 * i + 1] = JSON.toJSONString(map.get(key));
                i++;
            }
            return jedis.mset(keyValues);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
     * 即使只有一个给定 key 已存在， MSETNX 也会拒绝执行所有给定 key 的设置操作。
     *
     * @param map
     * @param <T>
     * @return
     */
    public <T> Long msetnx(final Map<String, T> map) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            String[] keyValues = new String[map.size() * 2];
            int i = 0;
            for (String key : map.keySet()) {
                keyValues[2 * i] = key;
                keyValues[2 * i + 1] = JSON.toJSONString(map.get(key));
                i++;
            }
            return jedis.msetnx(keyValues);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public int hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            return jedis.hset(key, field, value).intValue();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回哈希表 key 中给定域 field 的值。
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            return jedis.hget(key, field);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回集合 key 中的所有成员。
     * 不存在的 key 被视为空集合。
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            return jedis.smembers(key);
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
    }

    /**
     * 返回哈希表 key 中，所有的域和值。
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            return jedis.hgetAll(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 查找所有符合给定模式 pattern 的 key 。
     * KEYS * 匹配数据库中所有 key 。
     * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
     * KEYS h*llo 匹配 hllo 和 heeeeello 等。
     * KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
     * 特殊符号用 \ 隔开
     *
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();

            return jedis.keys(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param hashKey
     * @param taskId
     * @return
     */
    public long hdel(String hashKey, String taskId) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            return jedis.hdel(hashKey, taskId);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 批量从list中删除项
     *
     * @param key
     * @param elements list中的element，即需要删除的项
     * @return
     */
    public long removeElementFormList(String key, String... elements) {
        long result = 0L;
        for (String element : elements) {
            result += lrem(key, 0, element);
        }
        return result;

    }

    /**
     * 从list中删除指定Value
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0 : 移除表中所有与 VALUE 相等的值。
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lrem(String key, long count, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrem(key, count, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 批量从set中删除项
     *
     * @param key
     * @param element list中的element，即需要删除的项
     * @return
     */
    public long removeElementFormSet(String key, String... element) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, element);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key
     * @param args
     * @return
     */
    public long lpush(String key, String... args) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, args);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     *
     * @param key
     * @param timout
     * @return
     */
    public List<String> brpop(String key, int timout) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timout, key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 推入消息到redis消息通道
     *
     * @param channel
     * @param message
     */
    public void publish(byte[] channel, byte[] message) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.publish(channel, message);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     * 监听消息通道
     *
     * @param jedisPubSub - 监听任务
     * @param channels    - 要监听的消息通道
     */
    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(jedisPubSub, channels);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 监听消息通道
     *
     * @param jedisPubSub - 监听任务
     * @param channels    - 要监听的消息通道
     */
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(jedisPubSub, channels);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> sort(String key, String condition, boolean isAsc) {


        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            //String key="MT-5-5-0";

            StringBuilder sb = new StringBuilder();
            sb.append(key);
            sb.append("-*->");
            sb.append(condition);

            SortingParams sortingParams = new SortingParams();

            sortingParams.by(sb.toString());
            if (isAsc) {
                sortingParams.asc();
            } else {
                sortingParams.desc();
            }

            List<String> sort = jedis.sort(key, sortingParams);
            return sort;

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
