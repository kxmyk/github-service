package com.example.githubservice.exception;

public class GithubClientException extends RuntimeException {
    public GithubClientException(String message) {
        super(message);
    }
}
