package com.example.githubservice.service;

import com.example.githubservice.dto.BranchDto;
import com.example.githubservice.dto.RepositoryDto;
import com.example.githubservice.exception.GithubClientException;
import com.example.githubservice.exception.UserNotFoundException;
import com.example.githubservice.model.GithubBranch;
import com.example.githubservice.model.GithubRepo;
import io.smallrye.mutiny.Uni;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GithubService {

    private final WebClient webClient;

    public GithubService(WebClient githubWebClient) {
        this.webClient = githubWebClient;
    }

    public Uni<List<RepositoryDto>> getRepositories(String username) {
        return Uni.createFrom().publisher(fetchRepositories(username));
    }

    private Mono<List<RepositoryDto>> fetchRepositories(String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(GithubRepo.class)
                .filter(repo -> !repo.fork())
                .flatMap(repo -> fetchBranches(repo)
                        .map(branches -> new RepositoryDto(repo.name(), repo.owner().login(), branches)))
                .collectList()
                .onErrorMap(WebClientResponseException.NotFound.class, ex ->
                        new UserNotFoundException("User not found: " + username))
                .onErrorMap(WebClientResponseException.class, ex ->
                        new GithubClientException("Error fetching data from GitHub: " + ex.getMessage()));
    }

    private Mono<List<BranchDto>> fetchBranches(GithubRepo repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}/branches", repo.owner().login(), repo.name())
                .retrieve()
                .bodyToFlux(GithubBranch.class)
                .map(branch -> new BranchDto(branch.name(), branch.commit().sha()))
                .collectList();
    }
}
