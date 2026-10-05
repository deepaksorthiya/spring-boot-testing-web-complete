package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * a simple smoke test to check if application is ready to accept requests or not
 */
class SmokeTestWithHttpRequestTests extends AbstractIntegrationTestBase {

    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        restTestClient
                .get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectAll(
                        responseSpec -> responseSpec.expectStatus().isOk(),
                        responseSpec -> responseSpec.expectBody(String.class).isEqualTo("default hello message")
                );
    }
}
