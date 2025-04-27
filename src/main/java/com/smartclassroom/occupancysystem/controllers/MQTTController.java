package com.smartclassroom.occupancysystem.controllers;

import com.amazonaws.services.iot.client.AWSIotException;
import com.smartclassroom.occupancysystem.models.SmartClassroomPayload;
import com.smartclassroom.occupancysystem.services.MQTTPubSubService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MQTTController {
    private final MQTTPubSubService mqttPubSubService;

    public MQTTController(MQTTPubSubService mqttPubSubService) {
        this.mqttPubSubService = mqttPubSubService;
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestBody SmartClassroomPayload smartClassroomPayload) throws AWSIotException {
        mqttPubSubService.publishMessage(smartClassroomPayload);
        return "Message published successfully";
    }
}
