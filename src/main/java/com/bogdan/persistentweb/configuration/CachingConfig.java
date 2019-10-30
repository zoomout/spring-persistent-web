package com.bogdan.persistentweb.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ConditionalOnProperty(
    value = "cache.enabled",
    havingValue = "true"
)
public class CachingConfig {

  public static final String CUSTOMER_CACHE = "com.bogdan.persistentweb.domain.Customer";
  public static final String PRODUCT_CACHE = "com.bogdan.persistentweb.domain.Product";

  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager(CUSTOMER_CACHE, PRODUCT_CACHE);
  }

}
