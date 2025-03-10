package com.example.githubservice.client;

import com.example.githubservice.model.GithubBranch;
import com.example.githubservice.model.GithubRepo;
import com.example.githubservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GithubClient {
    private final WebClient webClient;

    public GithubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<GithubRepo> getRepositories(String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        Mono.error(new UserNotFoundException("User not found: " + username)))
                .bodyToFlux(GithubRepo.class)
                .filter(repo -> !repo.isFork());
    }

    public Flux<GithubBranch> getBranches(String branchesUrl) {
        return webClient.get()
                .uri(branchesUrl.replace("{/branch}", ""))
                .retrieve()
                .bodyToFlux(GithubBranch.class);
    }
}
