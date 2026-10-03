package com.example.greeting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class GreetingRepo {

    private final String message;

    public GreetingRepo(@Value("${greet.message:NA}") String message) {
        this.message = message;
    }

    public String greet() {
        return message;
    }

}
