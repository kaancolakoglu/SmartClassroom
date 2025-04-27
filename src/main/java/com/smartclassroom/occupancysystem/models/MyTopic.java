package com.smartclassroom.occupancysystem.models;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartclassroom.occupancysystem.services.MessageProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyTopic extends AWSIotTopic {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final MessageProcessor messageProcessor;


    public MyTopic(String topic, AWSIotQos qos, ObjectMapper objectMapper, MessageProcessor messageProcessor) {
        super(topic, qos);
        this.objectMapper = objectMapper;
        this.messageProcessor = messageProcessor;
    }
    @Override
    public void onMessage(AWSIotMessage message) {
        try {
            log.info("Received message {}", message.getStringPayload());
            SmartClassroomPayload payload = objectMapper.readValue(message.getStringPayload(), SmartClassroomPayload.class);
            messageProcessor.processMessage(payload);
        } catch (Exception e) {
            log.error("Error processing message {}", e.getMessage());
        }

        log.info(message.toString());
    }
}


