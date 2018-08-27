package ru.integrations.commons;

public enum Headers {

    /**
     * This header MUST BE filled with instance id of source service
     */
    GPB_SOURCE_INSTANCE_ID,

    /**
     * This header MUST BE filled with name of topic (value must be from  configuration file  (e.g application .yml) -  ${kafka.groupConsumer.topic} or ${kafka.specificConsumer.topic})
     */
    GPB_REPLY_TOPIC_NAME,

    /**
     * This header is only for specific topic
     * <p>
     * If this value contains instance id of service than this message will be consumed by only one consumer
     * or
     * If contains  "*" than message will be consumed by all instances of consumers
     */
    GPB_DESTINATION_INSTANCE_ID,

    /**
     * random UUUID v.4
     */
    GPB_MESSAGE_ID,

    /**
     * This header MUST BE filled with MESSAGE_ID from income requestClient
     * For linked requestClient-response
     */
    GPB_CORRELATION_ID,

    /**
     * Version of DTO (e.g 1,2 etc). But note  that backward compatibility is preferred
     */
    GPB_VERSION,


    /**
     * 0 - success
     * or else - error
     */
    GPB_PROCESS_RESULT,

    /**
     * This header MUST BE FILLED when PROCESS_RESULT is not equal 0.
     */
    GPB_ERROR_DESCRIPTION;

}
