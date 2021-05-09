package jayfeng.com.meituan.courier.loginregistry.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 对 redis 缓存的增删改查操作
 * @author JayFeng
 * @date 2021/4/29
 */
@Component
public class RedisOperate {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 存入 key-value
     * @param key 键
     * @param value 值
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 存入 key-value 设置过期时间
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     */
    public void set(String key, String value, Long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 存入 key-value 设置过期时间和时间单位
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, String value, Long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 向某个 map 存入 key-value
     * @param hashKey key 为这个 的 map
     * @param addKey 存入的 key
     * @param addValue 存入的 value
     */
    public void setForHash(String hashKey, Object addKey, Object addValue) {
        stringRedisTemplate.opsForHash().put(hashKey, addKey, addValue);
    }

    /**
     * 拿取数据
     * @param key 键
     * @return 返回 value 值
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 获取某一个 key 的过期时间
     * @param key 键
     * @param timeUnit 时间单位
     * @return 返回过期时间
     */
    public Long getTimeout(String key, TimeUnit timeUnit) {
        return stringRedisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 是否存在这个 key
     * @param key 键
     * @return 返回是否存在
     */
    public Boolean isExistsKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 某个 map 中是否存在某个 key
     * @param hashKey 哪一个 map
     * @param key 哪一个 key
     * @return 返回是否存在
     */
    public Boolean isExistsKeyForHash(String hashKey, Object key) {
        return stringRedisTemplate.opsForHash().hasKey(hashKey, key);
    }

    /**
     * 获取某个 map 中的某个键值对的值
     * @param hashKey map key
     * @param key 键
     * @return 返回 value
     */
    public Object getValueForHash(String hashKey, Object key) {
        return stringRedisTemplate.opsForHash().get(hashKey, key);
    }

    /**
     * 删除 key-value
     * @param key 键
     * @return 返回是否删除成功
     */
    public Boolean remove(String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * 删除某个 map 中的某个 key
     * @param hashKey 哪一个 map
     * @param removeKey 哪一个 key
     */
    public void removeForHash(String hashKey, String removeKey) {
        stringRedisTemplate.opsForHash().delete(hashKey, removeKey);
    }

}
