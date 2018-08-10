package ru.gpb.rkk.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.gpb.rkk.listeners.GroupKafkaListener;
import ru.gpb.rkk.listeners.SpecificKafkaListener;
import ru.integrations.commons.Message;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    private KafkaConfig kafkaConfig;

    @Autowired
    public KafkaConfiguration(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @Bean
    public KafkaTemplate<String, Message> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Message> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaConfig.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public Map<String, Object> groupConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaConfig.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, "java.lang.String");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Message.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.kafkaConfig.getGroupConsumer().getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return props;
    }

    @Bean
    public Map<String, Object> specificConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaConfig.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, "java.lang.String");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Message.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.kafkaConfig.getSpecificConsumer().getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return props;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, Message> groupKafkaListenerContainer(
            GroupKafkaListener messageListener) {
        ContainerProperties containerProperties = new ContainerProperties(this.kafkaConfig.getGroupConsumer().getTopic());
        containerProperties.setMessageListener(messageListener);
        containerProperties.setPollTimeout(this.kafkaConfig.getGroupConsumer().getPollTimeout());
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL);
        ConcurrentMessageListenerContainer<String, Message> concurrentMessageListenerContainer = new ConcurrentMessageListenerContainer<>(groupConsumerFactory(), containerProperties);
        concurrentMessageListenerContainer.setConcurrency(this.kafkaConfig.getGroupConsumer().getConcurrency());
        return concurrentMessageListenerContainer;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, Message> specificKafkaListenerContainer(
            SpecificKafkaListener messageListener) {
        ContainerProperties containerProperties = new ContainerProperties(this.kafkaConfig.getSpecificConsumer().getTopic());
        containerProperties.setMessageListener(messageListener);
        containerProperties.setPollTimeout(this.kafkaConfig.getSpecificConsumer().getPollTimeout());
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL);
        ConcurrentMessageListenerContainer<String, Message> concurrentMessageListenerContainer = new ConcurrentMessageListenerContainer<>(specificConsumerFactory(), containerProperties);
        concurrentMessageListenerContainer.setConcurrency(this.kafkaConfig.getSpecificConsumer().getConcurrency());
        return concurrentMessageListenerContainer;
    }


    private ConsumerFactory<String, Message> groupConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(groupConsumerConfig());
    }

    private ConsumerFactory<String, Message> specificConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(specificConsumerConfig());
    }

}
