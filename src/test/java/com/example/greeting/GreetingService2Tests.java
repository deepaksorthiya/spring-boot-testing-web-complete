package com.example.greeting;

import com.example.AbstractBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Service Layer Integration Testing Using Spring Boot Application Context @see {@link AbstractBaseTest}
 * No Mock Object will be replaced and injected in app context at runtime
 */
@TestPropertySource(properties = {"spring.profiles.active=test"})
class GreetingService2Tests extends AbstractBaseTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    void greeting_should_return_greet_message_from_test_profile() {
        String greet = greetingService.greet();
        assertThat(greet).isEqualTo("test greet message");
    }

}
