package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthController {

    @GetMapping({"/public/health", "/api/health"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", Instant.now());
        health.put("service", "Yüklet API");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(ApiResponse.success(health));
    }

    @GetMapping({"/public/health/ping", "/api/health/ping"})
    public ResponseEntity<ApiResponse<String>> ping() {
        return ResponseEntity.ok(ApiResponse.success("pong"));
    }
}
