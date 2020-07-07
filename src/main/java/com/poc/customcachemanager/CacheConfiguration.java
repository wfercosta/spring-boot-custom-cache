package com.poc.customcachemanager;

import com.poc.customcachemanager.component.HTTPCacheConfiguration;
import com.poc.customcachemanager.component.HTTPCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Profile("bra-redis-cache-cloud")
public class CacheConfiguration {

    @Bean
    public RestTemplate cacheRestClient(RestTemplateBuilder builder,
                                        CacheConfigurationProperties configuration) {

        final CacheConfigurationProperties.Connection connection = configuration.getConnection();

        return builder
                .rootUri(connection.getBaseUrl())
                .setConnectTimeout(connection.getConnectionTimeout())
                .setReadTimeout(connection.getReadTimeout())
                .build();
    }

    @Bean
    @Primary
    public CacheManager httpCacheManager(@Qualifier("cacheRestClient") RestTemplate restTemplate,
                                         CacheConfigurationProperties configuration) {
        return HTTPCacheManager.Builder
                .fromRestTemplate(restTemplate)
                .disableCreateOnMissingCache()
                .withInitialCacheNames(Stream.of("cache_a", "cache_b", "cache_c").collect(Collectors.toSet()))
                .withCacheConfiguration("cache_d", HTTPCacheConfiguration.defaultConfiguration())
                .withCacheDefaults(configuration.getDefaults())
                .withInitialCacheConfigurations(configuration.getCaches()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> (HTTPCacheConfiguration) e )))
                .build();
    }

}
