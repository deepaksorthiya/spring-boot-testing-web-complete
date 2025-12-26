package com.example.greeting;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserSettingsService {

    public String getUserSettings() {
        boolean sub = new Random().nextBoolean();
        if (sub) {
            return "SUBSCRIBE";
        }
        return "UNSUBSCRIBE";
    }
}
