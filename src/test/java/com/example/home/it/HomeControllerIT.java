package com.example.home.it;

import com.example.AbstractIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.EntityExchangeResult;

import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerIT extends AbstractIntegrationTestBase {

    @Test
    void message_should_return_status_ok_and_home_message() {
        EntityExchangeResult<String> hello = restTestClient
                .get()
                .uri("/")
                .exchange()
                .returnResult(String.class);
        assertThat(hello.getResponseBody
                ()).isEqualTo("default hello message");
        assertThat(hello.getStatus()).isEqualTo(HttpStatus.OK);
    }
}
