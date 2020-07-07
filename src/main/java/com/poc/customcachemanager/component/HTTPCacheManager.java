package com.poc.customcachemanager.component;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class HTTPCacheManager extends AbstractTransactionSupportingCacheManager {

    private HTTPCacheWriter cacheWriter;
    private HTTPCacheConfiguration defaultCacheConfiguration;
    private boolean allowInFlightCacheCreation;
    private Map<String, HTTPCacheConfiguration> initialCacheConfiguration;

    private HTTPCacheManager(HTTPCacheWriter cacheWriter, HTTPCacheConfiguration defaultCacheConfiguration, Map<String, HTTPCacheConfiguration> initialCacheConfiguration, boolean allowInFlightCacheCreation) {
        requireNonNull(cacheWriter, "Cache writer is required");
        requireNonNull(defaultCacheConfiguration, "Default cache configuration is required");
        requireNonNull(initialCacheConfiguration, "Initial cache configurations are required");
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
        this.allowInFlightCacheCreation = allowInFlightCacheCreation;
        this.initialCacheConfiguration = initialCacheConfiguration;
    }

    @Override
    protected Collection<Cache> loadCaches() {
        return this.initialCacheConfiguration.entrySet().stream()
                .map(entry -> this.createHttpCache(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    protected Cache getMissingCache(String name) {
        return this.allowInFlightCacheCreation? this.createHttpCache(name, this.defaultCacheConfiguration) : null;
    }

    private Cache createHttpCache(String name, HTTPCacheConfiguration cacheConfiguration) {
        return new HTTPCache(name,
                this.cacheWriter,
                isNull(cacheConfiguration) ? this.defaultCacheConfiguration : cacheConfiguration);
    }

    public static class Builder {

        private HTTPCacheWriter cacheWriter;
        private HTTPCacheConfiguration cacheConfiguration;
        private boolean allowInFlightCacheCreation;
        private Map<String, HTTPCacheConfiguration> initialCaches;

        private Builder() {
            this.cacheConfiguration = HTTPCacheConfiguration.defaultConfiguration();
            this.initialCaches = new LinkedHashMap<>();
            this.allowInFlightCacheCreation = true;
        }

        private Builder(HTTPCacheWriter cacheWriter) {
            this();
            requireNonNull(cacheWriter, "Cache writer is required");
            this.cacheWriter = cacheWriter;
        }

        public static Builder fromRestTemplate(RestTemplate restTemplate) {
            requireNonNull(restTemplate, "Rest template is required");
            return new Builder(HTTPCacheWriter.defaultCacheWriter(restTemplate));
        }

        public static Builder fromHTTPCacheWriter(HTTPCacheWriter cacheWriter) {
            requireNonNull(cacheWriter, "Cache writer is required");
            return  new Builder(cacheWriter);
        }

        public Builder withCacheDefaults(HTTPCacheConfiguration defaultCacheConfiguration) {
            requireNonNull(defaultCacheConfiguration, "Cache configuration is required");
            this.cacheConfiguration = defaultCacheConfiguration;
            return this;
        }

        public Builder withInitialCacheConfigurations(Map<String, HTTPCacheConfiguration> cacheConfigurations) {
            requireNonNull(cacheConfigurations, "Cache configurations are required");
            cacheConfigurations.forEach((cacheName, configuration) ->
                    requireNonNull(configuration, String.format("Cache configuration is required for cache %s", cacheName)));
            this.initialCaches.putAll(cacheConfigurations);
            return this;
        }

        public Builder withInitialCacheNames(Set<String> cacheNames) {
            requireNonNull(cacheNames, "Cache names is required");
            cacheNames.forEach(cacheName -> this.withCacheConfiguration(cacheName, this.cacheConfiguration));
            return this;
        }

        public Builder withCacheConfiguration(String cacheName, HTTPCacheConfiguration cacheConfiguration) {
            requireNonNull(cacheName, "Cache name is required");
            requireNonNull(cacheConfiguration, "Cache configuration is required");
            this.initialCaches.put(cacheName, cacheConfiguration);
            return this;
        }

        public Builder disableCreateOnMissingCache() {
            this.allowInFlightCacheCreation = false;
            return this;
        }

        public HTTPCacheManager build() {
            return new HTTPCacheManager(this.cacheWriter,
                    this.cacheConfiguration, this.initialCaches, this.allowInFlightCacheCreation);
        }

    }

}
