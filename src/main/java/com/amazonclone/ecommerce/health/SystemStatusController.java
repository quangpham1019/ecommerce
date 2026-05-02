package com.amazonclone.ecommerce.health;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemStatusController {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "application", applicationName,
                "timestamp", Instant.now(),
                "status", "UP"
        );
    }
}

