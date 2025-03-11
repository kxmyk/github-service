package com.example.githubservice.controller;

import com.example.githubservice.dto.RepositoryDto;
import com.example.githubservice.exception.ErrorResponse;
import com.example.githubservice.exception.UserNotFoundException;
import com.example.githubservice.service.GithubService;
import io.smallrye.mutiny.Uni;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
public class GithubController {
    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/repos/{username}")
    public Uni<List<RepositoryDto>> getRepositories(@PathVariable String username) {
        return githubService.getRepositories(username);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse(404, ex.getMessage());
    }
}
