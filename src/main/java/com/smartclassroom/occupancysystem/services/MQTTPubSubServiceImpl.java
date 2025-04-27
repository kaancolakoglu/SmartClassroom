package com.smartclassroom.occupancysystem.services;

import com.amazonaws.services.iot.client.AWSIotException;
import com.smartclassroom.occupancysystem.configs.MQTTConfig;
import com.smartclassroom.occupancysystem.models.SmartClassroomPayload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class MQTTPubSubServiceImpl implements MQTTPubSubService {
    private final MQTTConfig mqttConfig;

    public MQTTPubSubServiceImpl(MQTTConfig mqttConfig) {
        this.mqttConfig = mqttConfig;
    }

    @Override
    public void publishMessage(@RequestBody SmartClassroomPayload smartClassroomPayload) throws AWSIotException {
        mqttConfig.connectToIoT();
        mqttConfig.publish(smartClassroomPayload);
    }

    public void subscribeToTopic() throws AWSIotException {
        mqttConfig.connectToIoT();
        mqttConfig.subscribe();
    }
}
