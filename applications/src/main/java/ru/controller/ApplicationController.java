package ru.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApplicationController {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/application/info", method= RequestMethod.GET)
    public String applicationInfo() {
        return "Application " + restTemplate.getForObject("https://customers/", String.class);
    }


}
