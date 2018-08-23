package ru.gpb.rkk.controllers;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;

import org.camunda.bpm.engine.repository.DeploymentBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gpb.rkk.config.KafkaConfig;
import ru.integrations.commons.Headers;
import ru.integrations.commons.Message;
import org.springframework.web.multipart.MultipartFile;
import ru.integrations.dto.bpm.StartProcessRequestDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@RestController
public class BpmRestController {

    @Autowired
    private KafkaConfig kafkaConfig;

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @RequestMapping(value = "rest/deploy/create", method= RequestMethod.POST)
    public String applicationInfo( @RequestParam("file") MultipartFile uploadfile) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        if (uploadfile.isEmpty()) {
            return "please select a file!";
        }

        InputStream uploadFileStream = null;
        try {
            uploadFileStream = uploadfile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deploymentBuilder.addInputStream("diagram_1.bpmn", uploadFileStream).deploy().getId();
    }

    @RequestMapping("/test/kafka_send")
    public void sentToKafka() {
        //send message
        String processKey = "TestProcess";
        ProducerRecord<String, Message> producerRecord = new ProducerRecord<>(this.kafkaConfig.getProducers().getBpm().getGroupTopic(), UUID.randomUUID().toString(), new StartProcessRequestDto(processKey));
        producerRecord.headers().add(Headers.GPB_SOURCE_INSTANCE_ID.name(), this.kafkaConfig.getSpecificConsumer().getGroupId().getBytes());
        producerRecord.headers().add(Headers.GPB_REPLY_TOPIC_NAME.name(), this.kafkaConfig.getSpecificConsumer().getTopic().getBytes());
        producerRecord.headers().add(Headers.GPB_MESSAGE_ID.name(), UUID.randomUUID().toString().getBytes());
        producerRecord.headers().add(Headers.GPB_VERSION.name(), "1".getBytes());
        producerRecord.headers().add(KafkaHeaders.REPLY_TOPIC, this.kafkaConfig.getSpecificConsumer().getTopic().getBytes());
        this.kafkaTemplate.send(producerRecord);
    }
}
