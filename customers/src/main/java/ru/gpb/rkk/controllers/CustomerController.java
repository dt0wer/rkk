package ru.gpb.rkk.controllers;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gpb.rkk.config.KafkaConfig;
import ru.gpb.rkk.config.VaultConfiguration;
import ru.gpb.rkk.dto.VaultDto;
import ru.integrations.commons.Headers;
import ru.integrations.commons.Message;
import ru.integrations.dto.RequestApplicationDto;
import ru.integrations.dto.RequestCustomersDto;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for debug
 */
@RestController
public class CustomerController {

    public static Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final String getVersion;
    private final DiscoveryClient discoveryClient;
    private KafkaTemplate<String, Message> kafkaTemplate;
    private VaultConfiguration vaultConfiguration;
    private KafkaConfig kafkaConfig;

    @Autowired
    public CustomerController(String getVersion,
                              DiscoveryClient discoveryClient,
                              KafkaTemplate<String, Message> kafkaTemplate, VaultConfiguration configuration, KafkaConfig kafkaConfig) {
        this.getVersion = getVersion;
        this.discoveryClient = discoveryClient;
        this.kafkaTemplate = kafkaTemplate;
        this.vaultConfiguration = configuration;
        this.kafkaConfig = kafkaConfig;
    }

    @RequestMapping("/read_vault")
    public VaultDto checkVault() {
        return new VaultDto(this.vaultConfiguration.getUsername(), this.vaultConfiguration.getPassword());
    }

    @RequestMapping("/kafka_send")
    public void sentToKafka() {

        //send message
        String applicationId = UUID.randomUUID().toString();
        ProducerRecord<String, Message> producerRecord = new ProducerRecord<>(this.kafkaConfig.getProducers().getApplication().getGroupTopic(), UUID.randomUUID().toString(), new RequestApplicationDto(applicationId));
        producerRecord.headers().add(Headers.GPB_SOURCE_INSTANCE_ID.name(), this.kafkaConfig.getSpecificConsumer().getGroupId().getBytes());
        producerRecord.headers().add(Headers.GPB_REPLY_TOPIC_NAME.name(), this.kafkaConfig.getSpecificConsumer().getTopic().getBytes());
        producerRecord.headers().add(Headers.GPB_MESSAGE_ID.name(), UUID.randomUUID().toString().getBytes());
        producerRecord.headers().add(Headers.GPB_VERSION.name(), "1".getBytes());
        producerRecord.headers().add(KafkaHeaders.REPLY_TOPIC, this.kafkaConfig.getSpecificConsumer().getTopic().getBytes());

        this.kafkaTemplate.send(producerRecord);


        applicationId = UUID.randomUUID().toString();
        producerRecord = new ProducerRecord<>(this.kafkaConfig.getProducers().getApplication().getGroupTopic(), UUID.randomUUID().toString(), new RequestCustomersDto(applicationId));
        producerRecord.headers().add(Headers.GPB_SOURCE_INSTANCE_ID.name(), this.kafkaConfig.getSpecificConsumer().getGroupId().getBytes());
        producerRecord.headers().add(Headers.GPB_REPLY_TOPIC_NAME.name(), this.kafkaConfig.getSpecificConsumer().getTopic().getBytes());
        producerRecord.headers().add(Headers.GPB_MESSAGE_ID.name(), UUID.randomUUID().toString().getBytes());
        producerRecord.headers().add(Headers.GPB_VERSION.name(), "1".getBytes());
        producerRecord.headers().add(KafkaHeaders.REPLY_TOPIC, this.kafkaConfig.getSpecificConsumer().getTopic().getBytes());
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


    private Optional<URI> serviceUrl() {
        List<ServiceInstance> instances = discoveryClient.getInstances("application");
        return instances
                .stream()
                .map(si -> si.getUri()).findFirst();
    }
}
