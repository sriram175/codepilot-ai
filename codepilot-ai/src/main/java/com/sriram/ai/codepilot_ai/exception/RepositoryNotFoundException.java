package com.sriram.ai.codepilot_ai.exception;


public class RepositoryNotFoundException extends RuntimeException {

    public RepositoryNotFoundException(Long repositoryId) {
        super("Repository not found with id: " + repositoryId);
    }
}
