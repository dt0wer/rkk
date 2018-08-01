package ru.gpb.rkk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {

    private String getVersion;
    private DiscoveryClient discoveryClient;
    private RestTemplate loadbalancedRestTemplate;

    public Optional<URI> serviceUrl() {
        List<ServiceInstance> instances =  discoveryClient.getInstances("application");
        return instances
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

        String ip = null;
        try {
             ip  = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "IP "+ip+" ;Customer service version: " + getVersion;
    }

    @RequestMapping("/bah")
    public String bah() {
        return loadbalancedRestTemplate.getForObject("http://application/app/bah", String.class);
    }



    @RequestMapping("/discoveryClient")
    public String discoveryPing() throws RestClientException,
            ServiceUnavailableException {
//        URI service = serviceUrl()
//                .map(s -> s.resolve("/application/info"))
//                .orElseThrow(ServiceUnavailableException::new);

        return this.loadbalancedRestTemplate.getForObject("http://customers/", String.class);

    }

}
