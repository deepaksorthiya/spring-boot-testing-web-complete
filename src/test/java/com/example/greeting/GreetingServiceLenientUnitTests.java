package com.example.greeting;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @see <a href="https://codefarm0.medium.com/unnecessary-stubbings-detected-d21df974b675">UnnecessaryStubbingException Guide</a>
 * Service Layer Unit Testing Using Mockito where UnnecessaryStubbingException
 * @see org.mockito.exceptions.misusing.UnnecessaryStubbingException
 * @see MockitoSettings
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GreetingServiceLenientUnitTests {

    @InjectMocks
    private GreetingService greetingService;

    @Mock
    private GreetingRepo greetingRepo;

    @Mock
    private UserSettingsService userSettingsService;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        when(greetingRepo.greet()).thenReturn("Hello, Greeting World");
        when(userSettingsService.getUserSettings()).thenReturn("SUBSCRIBE");
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void greeting_should_return_greet_message() {
        String greet = greetingService.greet();
        assertEquals("Hello, Greeting World", greet);
        verify(greetingRepo, times(1)).greet();
        verify(userSettingsService, never()).getUserSettings();
    }

    @Test
    void getUserSettings_should_return_user_settings() {
        String greet = greetingService.getUserSettings();
        assertEquals("SUBSCRIBE", greet);
        verify(userSettingsService, times(1)).getUserSettings();
        verify(greetingRepo, never()).greet();
    }

}
