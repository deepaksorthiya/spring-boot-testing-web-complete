package com.example.greeting;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final GreetingRepo greetingRepo;
    private final UserSettingsService userSettingsService;

    public GreetingService(GreetingRepo greetingRepo, UserSettingsService userSettingsService) {
        this.greetingRepo = greetingRepo;
        this.userSettingsService = userSettingsService;
    }

    public String greet() {
        return greetingRepo.greet();
    }

    public String getUserSettings() {
        return userSettingsService.getUserSettings();
    }
}
