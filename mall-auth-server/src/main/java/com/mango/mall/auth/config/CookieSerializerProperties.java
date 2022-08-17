package com.mango.mall.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Mango
 * @Date 2022/6/9 19:50
 */
@ConfigurationProperties(prefix = "mall.session")
@Data
@Component
public class CookieSerializerProperties {
    private String domainName;
}
