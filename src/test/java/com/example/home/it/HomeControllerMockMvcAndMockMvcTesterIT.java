package com.example.home.it;

import com.example.AbstractBaseTest;
import com.example.home.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * integration testing for {@link HomeController} using mock web server with
 * {@link MockMvc} and {@link MockMvcTester} with live services, databases no mock beans
 */
@AutoConfigureMockMvc
class HomeControllerMockMvcAndMockMvcTesterIT extends AbstractBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void message_should_return_status_ok_and_home_message_with_mockMvc() throws Exception {
        this.mockMvc
                .perform(get("/").accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(equalTo("default hello message")),
                        jsonPath("$", is("default hello message")),
                        jsonPath("$").value("default hello message")
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
