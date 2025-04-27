package com.smartclassroom.occupancysystem.configs;

import com.amazonaws.services.iot.client.AWSIotException;
import com.smartclassroom.occupancysystem.services.MQTTPubSubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppConfig {
    @Bean
    public CommandLineRunner initSubscription(MQTTPubSubService mqttPubSubService) {
        return args -> {
            try {
                log.info("Subscribing to MQTT topics on startup...");
                mqttPubSubService.subscribeToTopic();
                log.info("Successfully subscribed to MQTT topics");
            } catch (AWSIotException e) {
                log.error("Failed to subscribe to MQTT topics on startup: {}", e.getMessage(), e);
            }
        };
    }
}
