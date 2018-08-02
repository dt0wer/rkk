package ru.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class ApplicationController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;
    @Value("${spring.cloud.consul.discovery.instanceId}")
    private String id;

    @RequestMapping(value = "/application/info", method= RequestMethod.GET)
    public String applicationInfo() {
        return "Application " + restTemplate.getForObject("http://customers/discoveryClient", String.class);
    }

    @RequestMapping(value = "/application", method= RequestMethod.GET)
    public String application() {
        return "/application " + id;
    }

    @RequestMapping(value = "/application/test/{var}", method= RequestMethod.GET)
    public String application(@PathVariable String var) {
        return "/application/test/"+ var + " " + id;
    }

    @RequestMapping(value = "/app/bah", method= RequestMethod.GET)
    public String applicationBah() {
        return "Application service " + id;
    }

    @RequestMapping(value = "/application/discovery", method= RequestMethod.GET)
    public String discovery() {

            List<ServiceInstance> list = discoveryClient.getInstances("customers");
            if (list != null && list.size() > 0 ) {
                ServiceInstance serviceInstance = list.get(0);
                return serviceInstance.getServiceId() + " " + serviceInstance.getHost() + ":" + serviceInstance.getPort();
            }
            return null;
    }




}
