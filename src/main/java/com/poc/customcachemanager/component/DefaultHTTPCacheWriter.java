package com.poc.customcachemanager.component;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

public class DefaultHTTPCacheWriter implements HTTPCacheWriter {

    private RestTemplate restTemplate;
    private HTTPCacheConfiguration cacheConfiguration;

    public DefaultHTTPCacheWriter(RestTemplate restTemplate) {
        requireNonNull(restTemplate, "Rest template is required");
        this.restTemplate = restTemplate;
    }

    @Override
    public void clean(String name, String... keys) {

    }

    @Override
    public String get(String name, String key) {
        return null;
    }

    @Override
    public void put(String name, String key, String value, Duration ttl) {

    }

    @Override
    public void putIfAbsent(String name, String key, String value, Duration ttl) {

    }

    @Override
    public void remove(String name, String key) {

    }
}
