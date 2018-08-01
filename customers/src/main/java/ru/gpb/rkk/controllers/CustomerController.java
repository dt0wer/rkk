package ru.gpb.rkk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.util.Optional;

@RestController
public class CustomerController {

    private String getVersion;
    private DiscoveryClient discoveryClient;
    private RestTemplate loadbalancedRestTemplate;

    public Optional<URI> serviceUrl() {
        return discoveryClient.getInstances("customers")
                .stream()
                .map(si -> si.getUri()).findFirst();
    }

    @Autowired
    public CustomerController(String getVersion, DiscoveryClient discoveryClient,RestTemplate loadbalancedRestTemplate) {
        this.getVersion = getVersion;
        this.discoveryClient = discoveryClient;
        this.loadbalancedRestTemplate = loadbalancedRestTemplate;
    }

    @RequestMapping("/")
    public String home() {
        return "Customer service version: " + getVersion;
    }

    @RequestMapping("/discoveryClient")
    public String discoveryPing() throws RestClientException,
            ServiceUnavailableException {
        URI service = serviceUrl()
                .map(s -> s.resolve("/"))
                .orElseThrow(ServiceUnavailableException::new);
        return this.loadbalancedRestTemplate.getForEntity(service, String.class)
                .getBody();
    }

}
