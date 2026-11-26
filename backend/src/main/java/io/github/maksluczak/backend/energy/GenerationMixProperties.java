package io.github.maksluczak.backend.energy;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "generation-mix")
public record GenerationMixProperties(String baseUrl, Duration connectTimeout, Duration readTimeout) {
}
