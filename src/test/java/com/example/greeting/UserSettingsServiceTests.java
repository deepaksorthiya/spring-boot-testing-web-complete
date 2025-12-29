package com.example.greeting;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

/// ## Approach 1: Refactoring (Recommended)
/// The cleanest solution is to inject the Random dependency.
/// This aligns with Spring Boot and clean architecture principles (Dependency Injection),
/// allowing you to pass a mock during testing.
/// ## Approach 2: Testing without Refactoring (Legacy Support)
/// If you cannot change the source code (e.g., it is legacy code), you must use Mockito-inline to mock the constructor of the Random class.
@ExtendWith(MockitoExtension.class)
class UserSettingsServiceTests {

    @InjectMocks
    private UserSettingsService userSettingsService;

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
    void getUserSettings_return_subscribe() {
        // Mock the construction of new Random()
        try (MockedConstruction<Random> _ = Mockito.mockConstruction(Random.class,
                (mock, _) -> {
                    // Define behavior for the newly created mock
                    Mockito.when(mock.nextBoolean()).thenReturn(true);
                })) {
            String result = userSettingsService.getUserSettings();
            Assertions.assertEquals("SUBSCRIBE", result);
        }
    }

    @Test
    void getUserSettings_return_unsubscribe() {
        try (MockedConstruction<Random> _ = Mockito.mockConstruction(Random.class,
                (mock, _) -> {
                    // Define behavior for the newly created mock
                    Mockito.when(mock.nextBoolean()).thenReturn(false);
                })) {
            String result = userSettingsService.getUserSettings();
            Assertions.assertEquals("UNSUBSCRIBE", result);
        }
    }
}