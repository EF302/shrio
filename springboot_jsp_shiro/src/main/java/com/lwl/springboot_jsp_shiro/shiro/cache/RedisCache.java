package com.lwl.springboot_jsp_shiro.shiro.cache;

import java.util.Collection;
import java.util.Set;

import com.lwl.springboot_jsp_shiro.utils.ApplicationContextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 自定义redis缓存实现
 *
 * @param <K>
 * @param <V>
 */
public class RedisCache<K, V> implements Cache<K, V> {

    private String cacheName;

    public RedisCache(String cacheName) {
        this.cacheName = cacheName;
    }

    public RedisCache() {
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("redis缓存——get key:" + k);
        return (V) this.getRedisTemplate().opsForHash().get(this.cacheName, k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        System.out.println("redis缓存——put key:" + k);
        System.out.println("redis缓存——put value:" + v);
//        //获取redis的内置对象
        //注 .var可快速生成左边属性类型与属性名
//        RedisTemplate redisTemplate = (RedisTemplate)ApplicationContextUtils.getBean("redisTemplate");
//        //把key的序列化方式改为String类型的序列化方式
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.opsForValue().set(k.toString(), v);
        //优化：用方法getRedisTemplate()封装获取redis的内置对象
        //opsForHash类似Map<K, Map<K, V>>
        //注 这里涉及序列化，entity类要可序列化，实现Serializable接口；盐值也需要可序列化
        this.getRedisTemplate().opsForHash().put(this.cacheName, k.toString(), v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        System.out.println("==========redis缓存——退出登录调用remove，不影响其他用户的缓存==========");
        return (V) this.getRedisTemplate().opsForHash().delete(this.cacheName, k.toString());
    }

    @Override
    public void clear() throws CacheException {
        System.out.println("==========redis缓存——clear==========");
        this.getRedisTemplate().delete(this.cacheName);
    }

    @Override
    public int size() {
        return this.getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        return this.getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
        return this.getRedisTemplate().opsForHash().values(this.cacheName);
    }

    /**
     * 封装获取redis内置对象方法
     *
     * @return
     */
    private RedisTemplate getRedisTemplate() {
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
