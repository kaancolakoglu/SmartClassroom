package com.smartclassroom.occupancysystem.configs;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
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

    public void connectToIoT() throws AWSIotException {
        AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
        client.connect();

        log.debug("Connected to IoT");
    }
}
