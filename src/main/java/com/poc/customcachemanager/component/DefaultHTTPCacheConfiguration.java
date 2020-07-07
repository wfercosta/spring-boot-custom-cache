package com.poc.customcachemanager.component;

import java.time.Duration;

public class DefaultHTTPCacheConfiguration implements  HTTPCacheConfiguration{

    private boolean allowCacheNullValues = true;
    private boolean usePrefix = false;
    private String prefix;
    private Duration ttl = Duration.ofMinutes(5);

    @Override
    public boolean isAllowCacheNullValues() {
        return false;
    }

    @Override
    public Duration getTtl() {
        return null;
    }

    @Override
    public boolean isUsePrefix() {
        return false;
    }

    @Override
    public String getPrefix() {
        return null;
    }
}
