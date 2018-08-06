package ru.gpb.rkk.controllers;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gpb.rkk.bus.event.TestRemoteApplicationEvent;

import javax.naming.ServiceUnavailableException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {

    @NotNull
    private final String getVersion;

    @NotNull
    private final DiscoveryClient discoveryClient;

    @NotNull
    private final RestTemplate loadbalancedRestTemplate;

    @NotNull
    private final ApplicationContext context;

    public Optional<URI> serviceUrl() {
        List<ServiceInstance> instances =  discoveryClient.getInstances("application");
        return instances
                .stream()
                .map(si -> si.getUri()).findFirst();
    }

    @Autowired
    public CustomerController(@NotNull String getVersion,
                              @NotNull DiscoveryClient discoveryClient,
                              @NotNull RestTemplate loadbalancedRestTemplate,
                              @NotNull ApplicationContext context) {
        this.getVersion = getVersion;
        this.discoveryClient = discoveryClient;
        this.loadbalancedRestTemplate = loadbalancedRestTemplate;
        this.context = context;
    }

    @HystrixCommand(fallbackMethod = "whitePage")
    @RequestMapping("/home")
    public String home() {

        String ip = null;
        try {
             ip  = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "IP "+ip+" ;Customer service version: " + getVersion;
    }

    public String whitePage() {
        return "Whitepage";
    }

    @RequestMapping("/bah")
    public String bah() {
        return loadbalancedRestTemplate.getForObject("http://application/app/bah", String.class);
    }



    @RequestMapping("/discoveryclient")
    public String discoveryPing() throws RestClientException,
            ServiceUnavailableException {
//        URI service = serviceUrl()
//                .map(s -> s.resolve("/application/info"))
//                .orElseThrow(ServiceUnavailableException::new);

        return this.loadbalancedRestTemplate.getForObject("http://customers/home", String.class);

    }

    @RequestMapping(value = "/event", method= RequestMethod.GET)
    public String event() {
        final String contextId = context.getId();
        final String message = "Hello from, customer service. time=" + new Date().toString();
        TestRemoteApplicationEvent event = new TestRemoteApplicationEvent(
                this,
                contextId,
                "/customers/**",
                message);
        context.publishEvent(event);
        return message;
    }

}
