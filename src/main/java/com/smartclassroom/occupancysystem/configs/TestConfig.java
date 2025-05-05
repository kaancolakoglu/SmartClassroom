package com.smartclassroom.occupancysystem.configs;

import com.smartclassroom.occupancysystem.services.MQTTPubSubService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestConfig {

    @MockBean
    private MQTTPubSubService mqttPubSubService;

    @Bean
    @Primary
    public MQTTConfig testMqttConfig() {
        return new MQTTConfig(null, null) {
            @Override
            public void connectToIoT() {
            }

            @Override
            public void subscribe() {
            }
        };
    }

    @Bean
    @Primary
    public CommandLineRunner testCommandLineRunner() {
        return args -> {
        };
    }
}
