package com.smartclassroom.occupancysystem.services;

import com.amazonaws.services.iot.client.AWSIotException;

public interface MQTTPubSubService {

    public void publishMessage() throws AWSIotException;
}
