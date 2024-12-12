package org.kea.easyscope;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBootTest(properties = "spring.profiles.active=test")
class EasyScopeApplicationTests {

    @Test
    void contextLoads() {
    }

}
