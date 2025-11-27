package com.example.greeting;

import com.example.AbstractBaseTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Service Layer Unit Testing Using Mockito Beans and Spring Boot Application Context @see {@link AbstractBaseTest}
 * Here Mock Beans will be replaced and injected in app context at runtime
 */
class GreetingService3Tests extends AbstractBaseTest {

    @Autowired
    private GreetingService greetingService;

    @MockitoBean
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
