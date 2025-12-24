package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;

/**
 * base class for performing integration testing on live server database etc.
 * you can override test specific properties in application-test.properties under test/resources
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=test"})
@AutoConfigureRestTestClient
public abstract class AbstractIntegrationTestBase {

    @Autowired
    public RestTestClient restTestClient;
}
