package com.mango.mall.auth.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @Author Mango
 * @Date 2022/6/9 19:37
 */
@EnableRedisHttpSession
@Configuration
public class MallSeessionConfig {
    @Bean
    public CookieSerializer cookieSerializer(CookieSerializerProperties serializerProperties) {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName(serializerProperties.getDomainName());
        return cookieSerializer;
    }

    @Bean
    public RedisSerializer redisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }
}
