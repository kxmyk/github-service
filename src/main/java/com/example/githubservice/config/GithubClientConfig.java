package com.example.githubservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GithubClientConfig {

    private final GithubProperties githubProperties;

    public GithubClientConfig(GithubProperties githubProperties) {
        this.githubProperties = githubProperties;
    }

    @Bean
    public WebClient githubWebClient(WebClient.Builder builder) {
        String token = githubProperties.getToken();
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("GitHub token is missing! Check application.properties or environment variables.");
        }
        return builder
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token " + token)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .build();
    }
}