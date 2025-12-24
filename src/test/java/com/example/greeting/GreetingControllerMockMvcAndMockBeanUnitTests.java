package com.example.greeting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit Testing for {@link GreetingController} using mockito
 */
@WebMvcTest(GreetingController.class)
class GreetingControllerMockMvcAndMockBeanUnitTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    GreetingService service;

    @BeforeEach
    void setUp() {
        when(service.greet()).thenReturn("Hello, Greeting Mock");
    }

    @Test
    void greeting_should_return_status_ok_and_greet_message_with_mockMvc() throws Exception {
        this.mockMvc
                .perform(get("/greeting"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(equalTo("Hello, Greeting Mock"))
                );
        verify(service).greet();
    }

    @Test
    void greeting_should_return_status_ok_and_greet_message_with_mockMvcTester() throws Exception {
        MvcTestResult testResult = mockMvcTester
                .get()
                .uri("/greeting")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(String.class)
                .isEqualTo("Hello, Greeting Mock");
        verify(service).greet();
    }

    @Test
    void greeting_should_return_status_ok_and_greet_message_with_mockMvcTester_when_diff_message_mock() throws Exception {
        reset(service);
        when(service.greet()).thenReturn("Greeting Mock");
        MvcTestResult testResult = mockMvcTester
                .get()
                .uri("/greeting")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(String.class)
                .isEqualTo("Greeting Mock");
        verify(service).greet();
    }
}
