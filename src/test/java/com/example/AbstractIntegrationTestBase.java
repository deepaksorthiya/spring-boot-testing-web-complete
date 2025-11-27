package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * base class for performing integration testing on live server database etc.
 * you can override test specific properties in application-test.properties under test/resources
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=test"})
@AutoConfigureTestRestTemplate
public abstract class AbstractIntegrationTestBase {

    @Autowired
    public TestRestTemplate testRestTemplate;
}
