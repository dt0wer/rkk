package ru.controller;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.bus.event.TestRemoteApplicationEvent;

import java.util.Date;
import java.util.List;

@RestController
public class ApplicationController {

    @NotNull
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationController.class);

    @NotNull
    private final ApplicationContext applicationContext;

    @NotNull
    private final RestTemplate restTemplate;

    @NotNull
    private final DiscoveryClient discoveryClient;

    @NotNull
    private final ServiceMatcher serviceMatcher;

    @NotNull
    private final String id;

    public ApplicationController(
            @NotNull ApplicationContext applicationContext, @NotNull RestTemplate restTemplate,
            @NotNull DiscoveryClient discoveryClient,
            @NotNull ServiceMatcher serviceMatcher, @NotNull @Value("${spring.cloud.consul.discovery.instanceId}") String id) {
        this.applicationContext = applicationContext;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        this.serviceMatcher = serviceMatcher;
        this.id = id;
    }

    @RequestMapping(value = "/application/info", method= RequestMethod.GET)
    public String applicationInfo() {
        return "Application " + restTemplate.getForObject("http://customers/discoveryclient", String.class);
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

    @RequestMapping(value = "/application/event/broadcast", method= RequestMethod.GET)
    public String eventBroadcast() {
        final String contextId = serviceMatcher.getServiceId();
        final String message = "BROADCAST: Hello from, appservice. time=" + new Date().toString();
        Object source = this;
        String destination = "*:**";
        TestRemoteApplicationEvent event = new TestRemoteApplicationEvent(
                source,
                contextId,
                destination,
                message);
        LOG.info("Publish event. source='{}', originService='{}', destinationService='{}', message='{}'",
                source, contextId, destination, message);
        applicationContext.publishEvent(event);
        return message;
    }

    @RequestMapping(value = "/application/event/customers", method= RequestMethod.GET)
    public String eventCustomers() {
        final String contextId = serviceMatcher.getServiceId();
        final String message = "CUSTOMERS: Hello from, appservice. time=" + new Date().toString();
        Object source = this;
        String destination = "customers:**";
        TestRemoteApplicationEvent event = new TestRemoteApplicationEvent(
                source,
                contextId,
                destination,
                message);
        LOG.info("Publish event. source='{}', originService='{}', destinationService='{}', message='{}'",
                source, contextId, destination, message);
        applicationContext.publishEvent(event);
        return message;
    }
}
