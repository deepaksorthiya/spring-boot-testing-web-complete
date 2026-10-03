package com.example.home;


import com.example.ApplicationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@EnableConfigurationProperties(ApplicationProperties.class)
public class HomeController {

    private final ApplicationProperties.HomeProperties homeProperties;

    public HomeController(ApplicationProperties appProperties) {
        this.homeProperties = appProperties.home();
    }

    @GetMapping
    public ResponseEntity<String> message() {
        return ResponseEntity.ok(homeProperties.message());
    }

}
