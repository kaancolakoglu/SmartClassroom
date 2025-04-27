package com.smartclassroom.occupancysystem.models;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyMessage extends AWSIotMessage {
    public MyMessage(String topic, AWSIotQos qos, String payload) {
        super(topic, qos, payload);
    }

    @Override
    public void onSuccess() {
        log.info("Successfully published message");
    }

    @Override
    public void onFailure() {
        log.info("Failed to publish message");
    }

    @Override
    public void onTimeout() {
        log.info("Timed out publishing message");
    }
}

