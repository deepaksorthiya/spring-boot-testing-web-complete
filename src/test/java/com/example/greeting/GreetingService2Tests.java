package com.example.greeting;

import com.example.AbstractBaseTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Service Layer Integration Testing Using Spring Boot Application Context @see {@link AbstractBaseTest}
 * No Mock Object will be replaced and injected in app context at runtime
 */
@TestPropertySource(properties = {"spring.profiles.active=test"})
class GreetingService2Tests extends AbstractBaseTest {

    @Autowired
    private GreetingService greetingService;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void greeting_should_return_greet_message_from_test_profile() {
        String greet = greetingService.greet();
        assertEquals("test greet message", greet);
    }

}
