package com.example.greeting;

import com.example.AbstractBaseTest;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Service Layer Unit Testing Using Mockito and Spring Boot Application Context @see {@link AbstractBaseTest}
 * Here Mock Object will be replaced and injected in app context at runtime
 *
 * @deprecated Not working after spring boot 4+
 */
@Disabled
@Deprecated
class GreetingServiceTests extends AbstractBaseTest {

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
