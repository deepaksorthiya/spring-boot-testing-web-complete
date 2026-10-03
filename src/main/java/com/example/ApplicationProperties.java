package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        @NestedConfigurationProperty HomeProperties home,
        @NestedConfigurationProperty GreetingProperties greet,
        @NestedConfigurationProperty PostProperties post
) {
    public record HomeProperties(@DefaultValue("NA") String message) {
    }

    public record GreetingProperties(@DefaultValue("NA") String message) {
    }

    public record PostProperties(@NestedConfigurationProperty ApiProperties api) {
        public record ApiProperties(@DefaultValue("https://jsonplaceholder.typicode.com") String baseUrl) {
        }
    }
}
