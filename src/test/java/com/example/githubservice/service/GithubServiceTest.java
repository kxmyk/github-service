package com.example.githubservice.service;

import com.example.githubservice.dto.RepositoryDto;
import com.example.githubservice.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GithubServiceTest {

    @Autowired
    private GithubService githubService;

    @Test
    void shouldHandleExistingAndNonExistingUsers() {
        String existingUser = "spring-projects";
        String nonExistingUser = "nonExistingUser123456";

        Mono<List<RepositoryDto>> existingUserResult = Mono.fromFuture(
                githubService.getRepositories(existingUser).subscribeAsCompletionStage()
        );

        Mono<List<RepositoryDto>> nonExistingUserResult = Mono.fromFuture(
                githubService.getRepositories(nonExistingUser).subscribeAsCompletionStage()
        );

        StepVerifier.create(existingUserResult)
                .assertNext(repositories -> {
                    assertThat(repositories).isNotEmpty();
                    repositories.forEach(repo -> {
                        assertThat(repo.name()).isNotNull();
                        assertThat(repo.ownerLogin()).isEqualTo(existingUser);
                        assertThat(repo.branches()).isNotEmpty();
                        repo.branches().forEach(branch -> {
                            assertThat(branch.name()).isNotNull();
                            assertThat(branch.lastCommitSha()).isNotNull();
                        });
                    });
                })
                .verifyComplete();

        StepVerifier.create(nonExistingUserResult)
                .expectError(UserNotFoundException.class)
                .verify();
    }
}
