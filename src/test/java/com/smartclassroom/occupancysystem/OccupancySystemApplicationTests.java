package com.smartclassroom.occupancysystem;

import com.smartclassroom.occupancysystem.configs.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class OccupancySystemApplicationTests {

    @Test
    void contextLoads() {
    }

}
