package com.web.wps.oauth.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/**
 * @author yibi
 */
@Configuration
public class TokenStoreConfig {
    /**
     * redis连接工厂
     */
    @Resource
    private JedisConnectionFactory jedisConnectionFactory;

    /**
     * 用于存放token
     */
    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(jedisConnectionFactory);
    }
}
