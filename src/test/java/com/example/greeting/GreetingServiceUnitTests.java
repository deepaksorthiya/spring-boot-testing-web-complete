package com.example.greeting;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.aot.DisabledInAotMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Service Layer Unit Testing Using Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisabledInAotMode
class GreetingServiceUnitTests {

    @InjectMocks
    private GreetingService greetingService;

    @Mock
    private GreetingRepo greetingRepo;

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
    void greeting_should_return_greet_message() {
        when(greetingRepo.greet()).thenReturn("Hello, Greeting World");
        String greet = greetingService.greet();
        assertEquals("Hello, Greeting World", greet);
        verify(greetingRepo, times(1)).greet();
    }

}
