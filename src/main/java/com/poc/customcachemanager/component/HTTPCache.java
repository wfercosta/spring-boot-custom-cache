package com.poc.customcachemanager.component;

import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;

import java.util.Objects;
import java.util.concurrent.Callable;

import static java.util.Objects.isNull;

public class HTTPCache extends AbstractValueAdaptingCache {

    private String name;
    private HTTPCacheWriter cacheWriter;
    private HTTPCacheConfiguration cacheConfiguration;


    public HTTPCache(String name, HTTPCacheWriter cacheWriter, HTTPCacheConfiguration cacheConfiguration) {
        super(Objects.nonNull(cacheConfiguration) ? cacheConfiguration.isAllowCacheNullValues() : false);
        Objects.requireNonNull(name, "A name is required");
        Objects.requireNonNull(cacheWriter, "A cache writer is required");
        Objects.requireNonNull(cacheConfiguration, "A cache configuration is required");

        this.name = name;
        this.cacheWriter = cacheWriter;
        this.cacheConfiguration = cacheConfiguration;
    }

    @Override
    protected Object lookup(Object key) {
        String value = this.cacheWriter.get(this.name, this.createAndConvertCacheKey(key));
        return Objects.nonNull(value)? this.deserializeCacheValue(value) : null;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.cacheWriter;
    }

    @Override
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper result = this.get(key);

        if (isNull(result)) {
            return (T) result.get();
        }

        T value = valueLoaderFrom(key, valueLoader);
        this.put(key, value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        Object cacheValue = this.preProcessCacheValue(value);
        if (!this.isAllowNullValues() && isNull(cacheValue)) {
            throw new IllegalArgumentException(String.format("Cache '%s' does not allow 'null' values.", this.name));
        }
        this.cacheWriter.put(this.name,
                this.createAndConvertCacheKey(key),
                this.serializeCacheValue(cacheValue),
                this.cacheConfiguration.getTtl());
    }

    @Override
    public void evict(Object key) {
        this.cacheWriter.remove(this.name, this.createAndConvertCacheKey(key));
    }

    @Override
    public void clear() {
        this.cacheWriter.clean(this.name, "*");
    }

    private String createAndConvertCacheKey(Object key) {
        return this.serializeCacheKey(this.createCacheKey(key));
    }

    private String createCacheKey(Object key) {
        String convertedKey = this.convertKey(key);
        return !this.cacheConfiguration.isUsePrefix()? convertedKey: this.prefixCacheKey(convertedKey);
    }

    private String convertKey(Object key) {

        if (key instanceof String) {
            return (String) key;
        }
        throw new IllegalStateException("Conversion for other formats not supported");
    }

    private String prefixCacheKey(String convertedKey) {
        return isNull(this.cacheConfiguration.getPrefix())? convertedKey :
                this.cacheConfiguration.getPrefix().concat("::").concat(convertedKey);
    }

    private String serializeCacheKey(String  key) {
        return null;
    }

    private String serializeCacheValue(Object cacheValue) {
        return null;
    }

    private Object deserializeCacheValue(String value) {
        return null;
    }

    private <T> T valueLoaderFrom(Object key, Callable<T> valueLoader) {
        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }

    private Object preProcessCacheValue(Object value) {
        if (value != null) {
            return value;
        }
        return this.isAllowNullValues() ? NullValue.INSTANCE : null;
    }

}
