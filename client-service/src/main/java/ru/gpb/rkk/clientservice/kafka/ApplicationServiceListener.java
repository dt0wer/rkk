package ru.gpb.rkk.clientservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.gpb.rkk.clientservice.configuration.kafka.KafkaConfig;
import ru.integrations.dto.ReponseCustomersDto;
import ru.integrations.dto.RequestApplicationDto;
import ru.integrations.dto.RequestCustomersDto;
import ru.integrations.dto.ResponseApplicationDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ru.integrations.commons.Headers.*;

@Service
@KafkaListener(containerFactory = "groupKafkaListenerContainerFactory",
        topics = {"${kafka.groupConsumer.topic}"})
@Slf4j
public class ApplicationServiceListener {

    @Autowired
    private KafkaConfig kafkaConfig;

    @KafkaHandler
    @SendTo("!{source.headers['kafka_replyTopic']}")
    public Message<ResponseApplicationDto> getApplication(RequestApplicationDto requestApplicationDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", requestApplicationDto);
        log.info("Headers: " + headers);
        Map<String, Object> outHeaders = new HashMap<>();
        outHeaders.put(GPB_SOURCE_INSTANCE_ID.name(), kafkaConfig.getSpecificConsumer().getGroupId());
        outHeaders.put(GPB_MESSAGE_ID.name(), UUID.randomUUID().toString());
        outHeaders.put(GPB_CORRELATION_ID.name(), headers.get(GPB_MESSAGE_ID.name()));
        outHeaders.put(GPB_VERSION.name(), headers.get(GPB_VERSION.name()));
        outHeaders.put(GPB_PROCESS_RESULT.name(), 0);

        String tn = new String((byte[]) headers.get(GPB_REPLY_TOPIC_NAME.name()));
        if (tn.contains("specific"))
            // individual response or broadcasting
            outHeaders.put(GPB_DESTINATION_INSTANCE_ID.name(), headers.get(GPB_SOURCE_INSTANCE_ID.name()));
        ack.acknowledge();
        return new GenericMessage<>(new ResponseApplicationDto("askdjlaksjd"), outHeaders);
    }

    @KafkaHandler
    @SendTo("!{source.headers['kafka_replyTopic']}")
    public Message<ReponseCustomersDto> getCustomer(RequestCustomersDto requestCustomersDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", requestCustomersDto);

        log.info("Headers: " + headers);
        Map<String, Object> outHeaders = new HashMap<>();
        outHeaders.put(GPB_SOURCE_INSTANCE_ID.name(), kafkaConfig.getSpecificConsumer().getGroupId());
        outHeaders.put(GPB_MESSAGE_ID.name(), UUID.randomUUID().toString());
        outHeaders.put(GPB_CORRELATION_ID.name(), headers.get(GPB_MESSAGE_ID.name()));
        outHeaders.put(GPB_VERSION.name(), headers.get(GPB_VERSION.name()));
        outHeaders.put(GPB_PROCESS_RESULT.name(), 0);

        String tn = new String((byte[]) headers.get(GPB_REPLY_TOPIC_NAME.name()));
        if (tn.contains("specific"))
            // individual response or broadcasting
            outHeaders.put(GPB_DESTINATION_INSTANCE_ID.name(), headers.get(GPB_SOURCE_INSTANCE_ID.name()));
        ack.acknowledge();
        return new GenericMessage<>(new ReponseCustomersDto("customerRespnse"), outHeaders);
    }
}