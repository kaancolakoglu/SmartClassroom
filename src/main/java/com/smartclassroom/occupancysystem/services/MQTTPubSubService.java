package com.smartclassroom.occupancysystem.services;

import com.amazonaws.services.iot.client.AWSIotException;
import com.smartclassroom.occupancysystem.models.SmartClassroomPayload;
import org.springframework.web.bind.annotation.RequestBody;

public interface MQTTPubSubService {

    public void publishMessage(@RequestBody SmartClassroomPayload smartClassroomPayload) throws AWSIotException;
}
