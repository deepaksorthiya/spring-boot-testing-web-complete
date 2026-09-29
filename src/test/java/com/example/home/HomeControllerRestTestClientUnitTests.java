package com.example.home;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

/**
 * Unit Testing for {@link HomeController} using @RestTestClient.
 * This {@link HomeController} has no dependencies so mock beans required.
 * The {@link AutoConfigureRestTestClient} annotation tells Spring Boot to
 * create and configure a RestTestClient that's bound to your MockMvc instance.
 * Without it, you'd need to create the client manually.
 */
@WebMvcTest(HomeController.class)
@AutoConfigureRestTestClient
class HomeControllerRestTestClientUnitTests {

    @Autowired
    RestTestClient restTestClient;

    @Test
    void message_should_return_status_ok_and_home_message() throws Exception {
        restTestClient
                .get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectAll(
                        responseSpec -> responseSpec.expectStatus().isOk(),
                        responseSpec -> responseSpec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        responseSpec -> responseSpec.expectBody(String.class).isEqualTo("default hello message")
                );
    }
}
