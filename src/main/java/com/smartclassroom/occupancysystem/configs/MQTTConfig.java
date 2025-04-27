package com.smartclassroom.occupancysystem.configs;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartclassroom.occupancysystem.models.MyMessage;
import com.smartclassroom.occupancysystem.models.MyTopic;
import com.smartclassroom.occupancysystem.models.SmartClassroomPayload;
import com.smartclassroom.occupancysystem.services.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MQTTConfig {
    private final ObjectMapper objectMapper;
    private final MessageProcessor messageProcessor;

    @Value("${CLIENT-ENDPOINT}")
    String clientEndpoint;
    @Value("${CLIENT-ID}")
    String clientId;
    @Value("${ACCESS-KEY-ID}")
    String awsAccessKeyId;
    @Value("${SECRET-ACCESS-KEY}")
    String awsSecretAccessKey;

    AWSIotMqttClient client = null;

    String topic = "topic_smart_classroom";
    AWSIotQos qos = AWSIotQos.QOS0;
    long timeout = 3000; // milliseconds

    @Autowired
    public MQTTConfig(ObjectMapper objectMapper, MessageProcessor messageProcessor) {
        this.objectMapper = objectMapper;
        this.messageProcessor = messageProcessor;
    }

    public void connectToIoT() throws AWSIotException {
        if (client == null || !client.getConnectionStatus().equals(AWSIotConnectionStatus.CONNECTED)) {
            client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
            client.connect();
            log.debug("Connected to IoT");
        }
    }
    public void publish(SmartClassroomPayload smartClassroomPayload) throws AWSIotException {
        MyMessage message = new MyMessage(topic, qos, smartClassroomPayload.toString());
        client.publish(message,timeout);
    }

    public void subscribe() throws AWSIotException {
        MyTopic myTopic = new MyTopic(topic, qos, objectMapper, messageProcessor);
        client.subscribe(myTopic);
        log.info("Subscribed to topic: {}", myTopic);
    }
}
