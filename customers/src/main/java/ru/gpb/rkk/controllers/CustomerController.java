package ru.gpb.rkk.controllers;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gpb.rkk.bus.event.TestRemoteApplicationEvent;
import ru.gpb.rkk.config.KafkaConfig;
import ru.gpb.rkk.config.VaultConfiguration;
import ru.gpb.rkk.dto.VaultDto;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CustomerController {

    private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @NotNull
    private final String getVersion;

    @NotNull
    private final DiscoveryClient discoveryClient;

    @NotNull
    private final RestTemplate loadbalancedRestTemplate;

    @NotNull
    private final ApplicationContext context;

    @NotNull
    private KafkaTemplate<String, String> kafkaTemplate;

    @NotNull
    private VaultConfiguration configuration;

    @NotNull
    private ConsulDiscoveryProperties properties;

    private KafkaConfig kafkaConfig;

    @Autowired
    public CustomerController(@NotNull String getVersion,
                              @NotNull DiscoveryClient discoveryClient,
                              @NotNull RestTemplate loadbalancedRestTemplate,
                              @NotNull ApplicationContext context, @NotNull KafkaTemplate<String, String> kafkaTemplate, @NotNull VaultConfiguration configuration, @NotNull ConsulDiscoveryProperties properties, KafkaConfig kafkaConfig) {
        this.getVersion = getVersion;
        this.discoveryClient = discoveryClient;
        this.loadbalancedRestTemplate = loadbalancedRestTemplate;
        this.context = context;
        this.kafkaTemplate = kafkaTemplate;
        this.configuration = configuration;
        this.properties = properties;
        this.kafkaConfig = kafkaConfig;
    }

    @RequestMapping("/read_vault")
    public VaultDto checkVault() {
        return new VaultDto(this.configuration.getUsername(), this.configuration.getPassword());
    }

    @RequestMapping("/kafka_send")
    public void sentToKafka() {

        //send message
        String message = "Hello world";
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("customersTopic", UUID.randomUUID().toString(), message);
        this.kafkaTemplate.send(producerRecord);
    }

    @HystrixCommand(fallbackMethod = "whitePage")
    @RequestMapping("/home")
    public String home() {

        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "IP " + ip + " ;Customer service version: " + getVersion;
    }

    @HystrixCommand(fallbackMethod = "whitePage")
    @RequestMapping("/check_hystrix")
    public String checkHystrix() {
        throw new RuntimeException("Url unavailable");
    }

    public String whitePage() {
        return "Whitepage";
    }

    @RequestMapping("/bah")
    public String bah() {
        return loadbalancedRestTemplate.getForObject("http://application/app/bah", String.class);
    }


    @RequestMapping("/discoveryclient")
    public String discoveryPing() throws RestClientException {
        return this.loadbalancedRestTemplate.getForObject("http://customers/home", String.class);

    }

    @RequestMapping(value = "/event", method = RequestMethod.GET)
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

    private Optional<URI> serviceUrl() {
        List<ServiceInstance> instances = discoveryClient.getInstances("application");
        return instances
                .stream()
                .map(si -> si.getUri()).findFirst();
    }
}
