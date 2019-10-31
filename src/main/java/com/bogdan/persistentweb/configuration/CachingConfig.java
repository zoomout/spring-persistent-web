package com.bogdan.persistentweb.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ConditionalOnProperty(
    value = "cache.enabled",
    havingValue = "true"
)
public class CachingConfig {
}
