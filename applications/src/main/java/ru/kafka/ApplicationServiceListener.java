package ru.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.integrations.commons.Message;
import ru.integrations.dto.RequestApplicationDto;
import ru.integrations.dto.ResponseApplicationDto;
@Service
@KafkaListener(containerFactory = "groupKafkaListenerContainerFactory",
               topics = {"${kafka.groupConsumer.topic}"})
@Slf4j
public class ApplicationServiceListener {
    @KafkaHandler
    public void getApplication(@Payload RequestApplicationDto requestApplicationDto, Acknowledgment ack) {
        log.info("Received group topic {}", requestApplicationDto);
        ack.acknowledge();
    }
}
