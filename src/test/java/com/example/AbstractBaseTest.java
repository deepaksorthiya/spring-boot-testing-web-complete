package com.example;

import com.example.support.PostgresTestSupport;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base class for tests that load the full application and PostgreSQL.
 */
@SpringBootTest
public abstract class AbstractBaseTest extends PostgresTestSupport {
}
