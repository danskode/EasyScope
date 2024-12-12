package org.kea.easyscope;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@SpringBootTest(properties = "spring.profiles.active=test")
public class BasicTest {

    // A simple test that will always pass
    @Test
    public void simpleTest() {
        assertTrue(true, "This test will always pass");
    }
}