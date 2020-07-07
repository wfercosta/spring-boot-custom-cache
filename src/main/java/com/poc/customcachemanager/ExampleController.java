package com.poc.customcachemanager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExampleController {

    private ExampleService service;

    public ExampleController(ExampleService service) {
        this.service = service;
    }

    @GetMapping("/resources")
    public List<String> index() {
        return this.service.findAll();
    }
}
