package com.poc.customcachemanager.component;

import java.time.Duration;

public interface HTTPCacheConfiguration {

    static HTTPCacheConfiguration defaultConfiguration() {
        return new DefaultHTTPCacheConfiguration();
    }

    boolean isAllowCacheNullValues();

    Duration getTtl();

    boolean isUsePrefix();

    String getPrefix();

}
