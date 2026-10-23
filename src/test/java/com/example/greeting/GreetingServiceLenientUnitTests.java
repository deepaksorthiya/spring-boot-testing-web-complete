package com.example.greeting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Service Layer Unit Testing using Mockito strict stubbing.
 * Stubbing per test method avoids UnnecessaryStubbingException without needing LENIENT strictness.
 */
@ExtendWith(MockitoExtension.class)
class GreetingServiceLenientUnitTests {

    @InjectMocks
    private GreetingService greetingService;

    @Mock
    private GreetingRepo greetingRepo;

    @Mock
    private UserSettingsService userSettingsService;

    @Test
    void greeting_should_return_greet_message() {
        when(greetingRepo.greet()).thenReturn("Hello, Greeting World");

        String greet = greetingService.greet();

        assertThat(greet).isEqualTo("Hello, Greeting World");
        verify(greetingRepo).greet();
        verifyNoInteractions(userSettingsService);
    }

    @Test
    void getUserSettings_should_return_user_settings() {
        when(userSettingsService.getUserSettings()).thenReturn("SUBSCRIBE");

        String settings = greetingService.getUserSettings();

        assertThat(settings).isEqualTo("SUBSCRIBE");
        verify(userSettingsService).getUserSettings();
        verifyNoInteractions(greetingRepo);
    }
}
