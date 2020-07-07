package com.poc.customcachemanager.component;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

public interface HTTPCacheWriter {

    static HTTPCacheWriter defaultCacheWriter(RestTemplate restTemplate) {
        return new DefaultHTTPCacheWriter(restTemplate);
    }

    void clean(String name, String... keys);

    String get(String name, String key);

    void put (String name, String key, String value, Duration ttl);

    void putIfAbsent(String name, String key, String value, Duration ttl);

    void remove(String name, String key);

}
