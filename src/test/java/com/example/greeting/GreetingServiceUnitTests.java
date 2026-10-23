package com.example.greeting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Service Layer Unit Testing Using Mockito
 */
@ExtendWith(MockitoExtension.class)
class GreetingServiceUnitTests {

    @InjectMocks
    private GreetingService greetingService;

    @Mock
    private GreetingRepo greetingRepo;

    @Test
    void greeting_should_return_greet_message() {
        when(greetingRepo.greet()).thenReturn("Hello, Greeting World");
        String greet = greetingService.greet();
        assertThat(greet).isEqualTo("Hello, Greeting World");
        verify(greetingRepo).greet();
    }
}
