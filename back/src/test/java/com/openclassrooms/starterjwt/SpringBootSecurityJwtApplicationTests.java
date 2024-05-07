package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {

    @Test
    public void contextLoads() {
        // Test that the Spring context loads successfully
        SpringBootSecurityJwtApplication.main(new String[]{}); // Run the application
    }
}
