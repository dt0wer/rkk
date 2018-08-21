package ru.gpb.rkk.clientservice.configuration.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfig {

    private Map<String, Object> properties;
    private String bootstrapServers;
    private Consumer groupConsumer;
    private Consumer specificConsumer;
    private  Producer application;
    private Producer bpm;

    public KafkaConfig() {
        this.properties = new HashMap<>();
    }

    @Data
    public static class Consumer {

        private String groupId;
        private Long pollTimeout;
        private Integer concurrency;
        private String topic;
    }

    @Data
    public static class Producer {

        private String groupTopic;
        private  String specificTopic;
    }
}
