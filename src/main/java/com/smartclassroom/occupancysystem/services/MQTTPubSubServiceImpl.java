package com.smartclassroom.occupancysystem.services;

import com.amazonaws.services.iot.client.AWSIotException;
import com.smartclassroom.occupancysystem.configs.MQTTConfig;
import org.springframework.stereotype.Service;

@Service
public class MQTTPubSubServiceImpl implements MQTTPubSubService {
    private final MQTTConfig mqttConfig;

    public MQTTPubSubServiceImpl(MQTTConfig mqttConfig) {
        this.mqttConfig = mqttConfig;
    }

    @Override
    public void publishMessage() throws AWSIotException {
        mqttConfig.connectToIoT();
    }
}
