package com.example.githubservice.model;

public record GithubRepo(String name, GithubOwner owner, String branchesUrl, boolean fork) {
    public boolean isFork() {
        return fork;
    }
}
