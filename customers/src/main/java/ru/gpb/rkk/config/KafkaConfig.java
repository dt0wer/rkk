package ru.gpb.rkk.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.gpb.rkk.listeners.KafkaListener;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig implements EnvironmentAware {

    private static final String KAFKA_BOOTSTRAP = "spring.kafka.bootstrap-servers";
    private static final String CONSUMER_GROUP_ID = "spring.kafka.consumer.group-id";
    private static final String CUSTOMERS_TOPIC = "spring.kafka.self.topic";
    private static final String POLL_TIMEOUT = "spring.kafka.consumer.pollTimeout";
    private static final String KAFKA_CONCURRENCY = "spring.kafka.consumer.concurrency";
    private Environment environment;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty(KAFKA_BOOTSTRAP));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.environment.getProperty(KAFKA_BOOTSTRAP));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.environment.getProperty(CONSUMER_GROUP_ID));
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return props;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> kafkaListenerContainer(
            KafkaListener messageListener) {
        ContainerProperties containerProperties = new ContainerProperties(this.environment.getProperty(CUSTOMERS_TOPIC));
        containerProperties.setMessageListener(messageListener);
        containerProperties.setPollTimeout(Long.parseLong(this.environment.getProperty(POLL_TIMEOUT)));
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL);
        ConcurrentMessageListenerContainer<String, String> concurrentMessageListenerContainer = new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);
        concurrentMessageListenerContainer.setConcurrency(Integer.parseInt(this.environment.getProperty(KAFKA_CONCURRENCY)));
        return concurrentMessageListenerContainer;
    }


    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
