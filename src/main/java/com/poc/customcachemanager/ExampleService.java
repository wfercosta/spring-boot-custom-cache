package com.poc.customcachemanager;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleService {

    @Cacheable(value = "res_strings")
    public List<String> findAll() {
        return null;
    }
}
