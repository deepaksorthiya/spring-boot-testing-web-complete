package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * this test is just to cover main method in test coverage
 */
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class SpringBootTestingWebCompleteApplicationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void main_method_should_load_context() {
        assertThat(applicationContext).isNotNull();
    }

}
