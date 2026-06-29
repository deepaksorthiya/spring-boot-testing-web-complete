package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * smoke test to check if application is starting successfully or not
 * its usually check if @see {@link ApplicationContext} created successfully or not
 */
class SmokeTests extends AbstractBaseTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void context_should_load() throws Exception {
        assertThat(applicationContext).isNotNull();
    }
}
