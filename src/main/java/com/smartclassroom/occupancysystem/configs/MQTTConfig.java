package com.smartclassroom.occupancysystem.configs;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.smartclassroom.occupancysystem.models.MyMessage;
import com.smartclassroom.occupancysystem.models.SmartClassroomPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MQTTConfig {

    @Value("${CLIENT-ENDPOINT}")
    String clientEndpoint;
    @Value("${CLIENT-ID}")
    String clientId;
    @Value("${ACCESS-KEY-ID}")
    String awsAccessKeyId;
    @Value("${SECRET-ACCESS-KEY}")
    String awsSecretAccessKey;

    AWSIotMqttClient client = null;

    public void connectToIoT() throws AWSIotException {
        client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
        client.connect();
        log.debug("Connected to IoT");
    }
    public void publish(SmartClassroomPayload smartClassroomPayload) throws AWSIotException {
        String topic = "topic_smart_classroom";
        AWSIotQos qos = AWSIotQos.QOS0;
        long timeout = 3000;                    // milliseconds

        MyMessage message = new MyMessage(topic, qos, smartClassroomPayload.toString());
        client.publish(message,timeout);
    }
}
