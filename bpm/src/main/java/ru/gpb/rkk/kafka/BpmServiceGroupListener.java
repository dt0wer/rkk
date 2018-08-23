package ru.gpb.rkk.kafka;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import ru.gpb.rkk.config.KafkaConfig;
import ru.integrations.dto.bpm.CompleteTasksRequestDto;
import ru.integrations.dto.bpm.CompleteTasksResponseDto;
import ru.integrations.dto.bpm.GetTasksRequestDto;
import ru.integrations.dto.bpm.GetTasksResponseDto;
import ru.integrations.dto.bpm.StartProcessRequestDto;
import ru.integrations.dto.bpm.StartProcessResponseDto;
import ru.integrations.dto.bpm.common.TaskDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ru.integrations.commons.Headers.*;

@Service
@KafkaListener(containerFactory = "groupKafkaListenerContainerFactory",
        topics = {"${kafka.groupConsumer.topic}"})
@SendTo()
@Slf4j
public class BpmServiceGroupListener {

    @Autowired
    private KafkaConfig kafkaConfig;

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Autowired
    public BpmServiceGroupListener(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @KafkaHandler
    public Message<StartProcessResponseDto> startProcess(StartProcessRequestDto startProcessRequestDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", startProcessRequestDto);
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

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(startProcessRequestDto.getProcessKey());
        return new GenericMessage<>(new StartProcessResponseDto(processInstance.getProcessInstanceId()), outHeaders);
    }

    @KafkaHandler
    public Message<GetTasksResponseDto> getTasks(GetTasksRequestDto getTasksRequestDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", getTasksRequestDto);
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

        ArrayList<TaskDto> tasksList = new ArrayList<>();
        TaskService taskService = processEngine.getTaskService();
        ArrayList<Task> camundaTasksList = (ArrayList<Task>) taskService.createTaskQuery().processInstanceId(getTasksRequestDto.getInstanceId()).active().list();
        for (Task camundaTask : camundaTasksList) {
            TaskDto task = new TaskDto();
            task.setId(camundaTask.getId());
            tasksList.add(task);
        }

        return new GenericMessage<>(new GetTasksResponseDto(tasksList), outHeaders);
    }

    @KafkaHandler
    public Message<CompleteTasksResponseDto> completeTasks(CompleteTasksRequestDto completeTasksRequestDto, @Headers Map<String, Object> headers, Acknowledgment ack) {
        log.info("Received group topic {}", completeTasksRequestDto);
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

        TaskService taskService = processEngine.getTaskService();
        for (String taskId: completeTasksRequestDto.getIds()) {
            taskService.complete(taskId);
        }

        return new GenericMessage<>(new CompleteTasksResponseDto(), outHeaders);
    }

}
