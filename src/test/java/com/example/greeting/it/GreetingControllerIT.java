package com.example.greeting.it;

import com.example.AbstractIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.EntityExchangeResult;

import static org.assertj.core.api.Assertions.assertThat;

class GreetingControllerIT extends AbstractIntegrationTestBase {

    @Test
    void greeting_should_return_greet_message_from_test_profile() {
        EntityExchangeResult<String> greeting = restTestClient
                .get()
                .uri("/greeting")
                .exchange()
                .returnResult(String.class);
        assertThat(greeting.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(greeting.getResponseBody()).isEqualTo("test greet message");
    }
}
