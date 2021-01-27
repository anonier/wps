package com.web.wps.util;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisUtils {
    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);
    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;
    @Value("${redis.key.prefix:ebi_web_dev}")
    private String redisKeyPrefix;

    public RedisUtils() {
    }

    public void put(Serializable objectTypeKey, Serializable objectKey, final Object o, Date expiry) {
        if (objectKey != null && o != null) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Put redis objectTypeKey is [%s],objectKey is [%s], value is [%s]", objectTypeKey, objectKey, o));
            }

            String fullObjectTypeKey = this.getFullObjectTypeKey(objectTypeKey);

            try {
                if (expiry != null) {
                    this.redisTemplate.expireAt(fullObjectTypeKey, expiry);
                }

                this.redisTemplate.opsForHash().put(fullObjectTypeKey, objectKey, o);
            } catch (RuntimeException var7) {
                log.error("Put redis error,fullObjectTypeKey is [" + fullObjectTypeKey + "],objectKey is [" + objectKey + "]," + var7.getMessage(), var7);
            }
        } else if (log.isDebugEnabled()) {
            log.debug("No redis put");
        }

    }

    public <T> T get(Serializable objectTypeKey, Serializable objectKey) {
        T returnO = null;
        if (objectKey != null) {
            String fullObjectTypeKey = this.getFullObjectTypeKey(objectTypeKey);
            Object o = null;

            try {
                o = this.redisTemplate.opsForHash().get(this.getFullObjectTypeKey(objectTypeKey), objectKey);
            } catch (RuntimeException var7) {
                log.error("Get redis error,fullObjectTypeKey is [" + fullObjectTypeKey + "],objectKey is [" + objectKey + "]," + var7.getMessage(), var7);
            }

            if (o != null) {
                returnO = (T) o;
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Get Put redis objectTypeKey is\t[%s],objectKey is [%s],value is [%s]", objectTypeKey, objectKey, o));
                }
            } else if (log.isDebugEnabled()) {
                log.debug(String.format("Not find redis objectTypeKey is [%s],objectKey is [%s]", objectTypeKey, objectKey));
            }
        }

        return returnO;
    }

    public void remove(Serializable objectTypeKey, Serializable objectKey) {
        if (objectKey != null) {
            String fullObjectTypeKey = this.getFullObjectTypeKey(objectTypeKey);

            try {
                this.redisTemplate.opsForHash().delete(fullObjectTypeKey, new Object[]{objectKey});
            } catch (RuntimeException var5) {
                log.error("Delete redis error,fullObjectTypeKey is [" + fullObjectTypeKey + "],objectKey is [" + objectKey + "]," + var5.getMessage(), var5);
            }
        }

    }

    private String getFullObjectTypeKey(Serializable objectTypeKey) {
        return this.redisKeyPrefix + "_" + objectTypeKey;
    }
}
