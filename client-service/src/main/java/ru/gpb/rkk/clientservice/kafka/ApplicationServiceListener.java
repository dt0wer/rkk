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

import ru.gpb.rkk.clientservice.domain.Client;
import ru.gpb.rkk.clientservice.configuration.kafka.KafkaConfig;
import ru.gpb.rkk.clientservice.service.ClientService;
import ru.integrations.dto.clientservice.RequestCreateClientDto;
import ru.integrations.dto.clientservice.ResponseCreateClientDto;
import ru.integrations.dto.clientservice.RequestGetClientIdDto;
import ru.integrations.dto.clientservice.ResponseGetClientIdDto;
import ru.integrations.dto.clientservice.RequestGetClientByIdDto;
import ru.integrations.dto.clientservice.ResponseGetClientByIdDto;
import ru.integrations.dto.clientservice.ClientDto;


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

    @Autowired
    ClientService clientService;

    @Autowired
    ClientMapper clientMapper;

    @KafkaHandler
    @SendTo("!{source.headers['kafka_replyTopic']}")
    public Message<ResponseCreateClientDto> createClient (RequestCreateClientDto requestCreateClientDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", requestCreateClientDto);
        log.info("Headers: " + headers);

        Map<String, Object> outHeaders = getOutHeaders (headers, kafkaConfig);

        String tn = new String((byte[]) headers.get(GPB_REPLY_TOPIC_NAME.name()));
        if (tn.contains("specific"))
            outHeaders.put(GPB_DESTINATION_INSTANCE_ID.name(), headers.get(GPB_SOURCE_INSTANCE_ID.name()));
        ack.acknowledge();
        ClientDto clientDtoRequest = requestCreateClientDto.getRequest();
        Client clientRequest = clientMapper.clientDtoToClient(clientDtoRequest);
        Client clientResponse = clientService.save(clientRequest);
        return new GenericMessage<>(new ResponseCreateClientDto(clientMapper.clientToClientDto(clientResponse)), outHeaders);
    }

    @KafkaHandler
    @SendTo("!{source.headers['kafka_replyTopic']}")
    public Message<ResponseGetClientIdDto> getClientId(RequestGetClientIdDto requestGetClientIdDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", requestGetClientIdDto);

        log.info("Headers: " + headers);

        Map<String, Object> outHeaders = getOutHeaders (headers, kafkaConfig);

        String tn = new String((byte[]) headers.get(GPB_REPLY_TOPIC_NAME.name()));
        if (tn.contains("specific"))
            // individual response or broadcasting
            outHeaders.put(GPB_DESTINATION_INSTANCE_ID.name(), headers.get(GPB_SOURCE_INSTANCE_ID.name()));
        ack.acknowledge();
        ClientDto request = requestGetClientIdDto.getRequest();
        Long responseId = clientService.getClientIdByNameBirthDateAndIdentityDoc(request.getName(), request.getSurname(), request.getPatronymic(),
                request.getDocumentType(),request.getDocumentSeries(),request.getDocumentNumber(),request.getBirthDate());

        return new GenericMessage<>(new ResponseGetClientIdDto(responseId), outHeaders);
    }

    @KafkaHandler
    @SendTo("!{source.headers['kafka_replyTopic']}")
    public Message<ResponseGetClientByIdDto> findClientById (RequestGetClientByIdDto requestGetClientByIdDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", requestGetClientByIdDto);
        log.info("Headers: " + headers);

        Map<String, Object> outHeaders = getOutHeaders (headers, kafkaConfig);

        String tn = new String((byte[]) headers.get(GPB_REPLY_TOPIC_NAME.name()));
        if (tn.contains("specific"))
            outHeaders.put(GPB_DESTINATION_INSTANCE_ID.name(), headers.get(GPB_SOURCE_INSTANCE_ID.name()));
        ack.acknowledge();
        Long request = requestGetClientByIdDto.getRequest();
        Client clientResponse = clientService.getCliendById(request);
        ClientDto clientDtoResponse = clientMapper.clientToClientDto(clientResponse);
        return new GenericMessage<>(new ResponseGetClientByIdDto(clientDtoResponse), outHeaders);
    }
    private Map<String, Object> getOutHeaders (Map<String, Object> headers, KafkaConfig kafkaConfig) {
        Map<String, Object> outHeaders = new HashMap<>();
        outHeaders.put(GPB_SOURCE_INSTANCE_ID.name(), kafkaConfig.getSpecificConsumer().getGroupId());
        outHeaders.put(GPB_MESSAGE_ID.name(), UUID.randomUUID().toString());
        outHeaders.put(GPB_CORRELATION_ID.name(), headers.get(GPB_MESSAGE_ID.name()));
        outHeaders.put(GPB_VERSION.name(), headers.get(GPB_VERSION.name()));
        outHeaders.put(GPB_PROCESS_RESULT.name(), 0);
        return outHeaders;
    }
}