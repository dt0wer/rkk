package ru.gpb.rkk.integrations.listeners;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class GroupKafkaListener implements AcknowledgingMessageListener<String, String> {

    private static Logger logger = LoggerFactory.getLogger(GroupKafkaListener.class);

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {

        try {
            processMessage(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acknowledgment.acknowledge();
        }
    }

    private void processMessage(ConsumerRecord<String, String> data) throws Exception {

        String messageValue = data.value();
        logger.info("Message: {}", messageValue);


    }
}
