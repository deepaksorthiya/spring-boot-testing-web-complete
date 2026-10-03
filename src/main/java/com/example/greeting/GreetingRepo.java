package com.example.greeting;


import com.example.ApplicationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Repository;

@Repository
@EnableConfigurationProperties(ApplicationProperties.class)
public class GreetingRepo {

    private final ApplicationProperties.GreetingProperties greetingProperties;

    public GreetingRepo(ApplicationProperties appProperties) {
        this.greetingProperties = appProperties.greet();
    }

    public String greet() {
        return greetingProperties.message();
    }

}
