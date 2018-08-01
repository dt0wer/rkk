package ru.gpb.rkk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private String getVersion;

    @Autowired
    public CustomerController(String getVersion) {
        this.getVersion = getVersion;
    }

    @RequestMapping("/")
    public String home() {
        return "Customer service version: " + getVersion;
    }
}
