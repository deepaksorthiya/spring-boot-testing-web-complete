package com.example.home;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit Testing for {@link HomeController} using mockito.
 * This {@link HomeController} has no dependencies so mock beans required.
 */
@WebMvcTest(HomeController.class)
class HomeControllerMockMvcAndMockBeanUnitTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void message_should_return_status_ok_and_home_message_with_mockMvc() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(equalTo("default hello message"))
                );
    }

    @Test
    void message_should_return_status_ok_and_home_message_with_mockMvcTester() throws Exception {
        MvcTestResult testResult = mockMvcTester
                .get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(String.class)
                .isEqualTo("default hello message");
    }
}
