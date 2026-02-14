package com.skh.controllers;

import com.skh.services.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestServiceImpl testService;

    @GetMapping("fetchAllEmployeesTesting")
    void fetchAllEmployees() {
        testService.fetchAllEmployees();

    }


}
