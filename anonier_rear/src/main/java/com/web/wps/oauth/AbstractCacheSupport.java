package com.web.wps.oauth;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import javax.annotation.Resource;

import com.web.wps.util.RedisUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

public abstract class AbstractCacheSupport<T> {
    private static final int DEFAULT_REDIS_TIME_TO_LIVE_SECONDS = 7200;
    @Resource
    private RedisUtils redisUtil;
    private Class<T> cacheClass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public AbstractCacheSupport() {
    }

    public abstract String getCacheId();

    public void put(Serializable key, T o) {
        Date expiry = null;
        if (this.getRedisTimeToLiveSeconds() > 0) {
            expiry = DateUtils.addSeconds(new Date(), this.getRedisTimeToLiveSeconds());
        }

        if (null != key && null != o) {
            this.redisUtil.put(this.getCacheId(), key, o, expiry);
        } else {
            throw new IllegalArgumentException("新增缓存的key和value不能为空");
        }
    }

    public void put(Serializable key, T o, int expiryTime) {
        Date expiry = null;
        if (expiryTime > 0) {
            expiry = DateUtils.addSeconds(new Date(), expiryTime);
        }

        if (null != key && null != o) {
            this.redisUtil.put(this.getCacheId(), key, o, expiry);
        } else {
            throw new IllegalArgumentException("新增缓存的key和value不能为空");
        }
    }

    public T get(Serializable key) {
        Assert.notNull(key, "获取缓存的key不能为空");
        return this.redisUtil.get(this.getCacheId(), key);
    }

    public void remove(Serializable key) {
        Assert.notNull(key, "移除缓存的key不能为空");
        this.redisUtil.remove(this.getCacheId(), key);
    }

    protected int getRedisTimeToLiveSeconds() {
        return 7200;
    }
}
