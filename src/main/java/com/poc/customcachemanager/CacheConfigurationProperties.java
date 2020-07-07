package com.poc.customcachemanager;

import com.poc.customcachemanager.component.HTTPCacheConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "bradesco.components.cache-manager")
public class CacheConfigurationProperties {

    private Connection connection;
    private Settings defaults;
    private Map<String, Settings> caches;

    public static class Connection {
        private String baseUrl;

        @DurationUnit(ChronoUnit.MILLIS)
        private Duration connectionTimeout = Duration.ofMillis(3000);

        @DurationUnit(ChronoUnit.MILLIS)
        private Duration readTimeout = Duration.ofMillis(3000);

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public Duration getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public Duration getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
        }
    }

    public static class Settings implements HTTPCacheConfiguration {

        private boolean allowCacheNullValues;
        private boolean usePrefix;
        private String prefix;

        @DurationUnit(ChronoUnit.MILLIS)
        private Duration ttl = Duration.ofMinutes(5);


        @Override
        public boolean isAllowCacheNullValues() {
            return allowCacheNullValues;
        }

        public void setAllowCacheNullValues(boolean allowCacheNullValues) {
            this.allowCacheNullValues = allowCacheNullValues;
        }

        @Override
        public boolean isUsePrefix() {
            return usePrefix;
        }

        public void setUsePrefix(boolean usePrefix) {
            this.usePrefix = usePrefix;
        }

        @Override
        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Duration getTtl() {
            return ttl;
        }

        public void setTtl(Duration ttl) {
            this.ttl = ttl;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Settings getDefaults() {
        return defaults;
    }

    public void setDefaults(Settings defaults) {
        this.defaults = defaults;
    }

    public Map<String, Settings> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, Settings> caches) {
        this.caches = caches;
    }
}
