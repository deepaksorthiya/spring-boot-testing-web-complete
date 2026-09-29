package com.example.greeting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit Testing for {@link GreetingController} using {@link RestTestClient}
 * The {@link AutoConfigureRestTestClient} annotation tells Spring Boot to
 * create and configure a RestTestClient that's bound to your MockMvc instance.
 * Without it, you'd need to create the client manually.
 */
@WebMvcTest(GreetingController.class)
@DisabledInAotMode
@AutoConfigureRestTestClient
class GreetingControllerRestTestClientUnitTests {

    @Autowired
    RestTestClient restTestClient;


    @MockitoBean
    GreetingService service;

    @BeforeEach
    void setUp() {
        when(service.greet()).thenReturn("Hello, Greeting Mock");
    }

    @Test
    void greeting_should_return_status_ok_and_greet_message() {
        this.restTestClient.get()
                .uri("/greeting")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectAll(
                        responseSpec -> responseSpec.expectStatus().isOk(),
                        responseSpec -> responseSpec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        responseSpec -> responseSpec.expectBody(String.class).isEqualTo("Hello, Greeting Mock")
                );
        verify(service).greet();
    }
}
