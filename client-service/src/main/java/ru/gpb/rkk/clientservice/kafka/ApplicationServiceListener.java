package ru.gpb.rkk.clientservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import ru.gpb.rkk.clientservice.domain.ClientEntity;
import ru.gpb.rkk.clientservice.configuration.kafka.KafkaConfig;
import ru.gpb.rkk.clientservice.mappers.ClientMapper;
import ru.gpb.rkk.clientservice.service.ClientService;

import ru.integrations.dto.clientservice.*;


import java.util.*;
import java.util.stream.Collectors;

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
        RequestClient requestClientRequest = requestCreateClientDto.getRequest();
        ClientEntity clientEntityRequest = clientMapper.requestClientDtoToClientEntity(requestClientRequest);
        ClientEntity clientEntityResponse = clientService.save(clientEntityRequest);
        ResponseCreateClientDto responseCreateClientDto = new ResponseCreateClientDto();
        responseCreateClientDto.setClientId(clientEntityResponse.getClientId());
        //TODO Clarify out how to deal with sourceSystemName and sourceRequestId parameters
        responseCreateClientDto.setSourceRequestId(requestCreateClientDto.getSourceRequestId());
        responseCreateClientDto.setSourceSystemName(requestCreateClientDto.getSourceSystemName());
        return new GenericMessage<>(responseCreateClientDto, outHeaders);
    }

    @KafkaHandler
    @SendTo("!{source.headers['kafka_replyTopic']}")
    public Message<ResponseGetClientDataDto> getClientId(RequestGetClientDataDto requestGetClientDataDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", requestGetClientDataDto);

        log.info("Headers: " + headers);

        Map<String, Object> outHeaders = getOutHeaders (headers, kafkaConfig);

        String tn = new String((byte[]) headers.get(GPB_REPLY_TOPIC_NAME.name()));
        if (tn.contains("specific"))
            // individual response or broadcasting
            outHeaders.put(GPB_DESTINATION_INSTANCE_ID.name(), headers.get(GPB_SOURCE_INSTANCE_ID.name()));
        ack.acknowledge();
        RequestClient request = requestGetClientDataDto.getRequestClient();
        List<Long> responseIds = clientService.getClientIdsByNameBirthDateAndIdentityDoc(request.getName(), request.getSurname(), request.getPatronymic(),
                request.getDocumentType(),request.getDocumentSeries(),request.getDocumentNumber(),request.getBirthdate());
        Client[] clients =  responseIds.stream().map(id -> new Client(id)).collect(Collectors.toList()).toArray(new Client[]{});
        ClientData clientData = new ClientData();
        clientData.setClient(clients);
        ResponseGetClientDataDto responseGetClientDataDto = new ResponseGetClientDataDto();
        responseGetClientDataDto.setClientData(clientData);
        //TODO Clarify out how to deal with sourceSystemName and sourceRequestId parameters
        responseGetClientDataDto.setSourceRequestId(requestGetClientDataDto.getSourceRequestId());
        responseGetClientDataDto.setSourceSystemName(requestGetClientDataDto.getSourceSystemName());
        return new GenericMessage<>(responseGetClientDataDto, outHeaders);
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