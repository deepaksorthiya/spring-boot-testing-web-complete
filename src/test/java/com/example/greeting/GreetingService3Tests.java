package com.example.greeting;

import com.example.AbstractBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Service Layer Unit Testing Using Mockito Beans and Spring Boot Application Context @see {@link AbstractBaseTest}
 * Here Mock Beans will be replaced and injected in app context at runtime
 */
@DisabledInAotMode
class GreetingService3Tests extends AbstractBaseTest {

    @Autowired
    private GreetingService greetingService;

    @MockitoBean
    private GreetingRepo greetingRepo;

    @Test
    void greeting_should_return_greet_message() {
        when(greetingRepo.greet()).thenReturn("Hello, Greeting World");
        String greet = greetingService.greet();
        assertThat(greet).isEqualTo("Hello, Greeting World");
        verify(greetingRepo).greet();
    }

}
