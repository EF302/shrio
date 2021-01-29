package com.lwl.springboot_jsp_shiro.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * 自定义shiro缓存管理器——参照EhCacheManager类，实现CacheManager接口
 */
public class RedisCacheManager implements CacheManager {

    /**
     * 获取缓存实例
     *
     * 注 可知缓存处理逻辑在该方法中实现，故需要自定义类去实现Cache接口
     *
     * @param cacheName
     * @param <K>
     * @param <V>
     * @return
     * @throws CacheException
     */
    @Override//参数cacheName：认证或授权缓存的统一名称
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        System.out.println("认证或授权缓存的统一名称：" + cacheName);//就是设置的名字  customerRealm.setAuthorizationCacheName("authorizationCache");
        return new RedisCache<K, V>(cacheName);
    }
}
