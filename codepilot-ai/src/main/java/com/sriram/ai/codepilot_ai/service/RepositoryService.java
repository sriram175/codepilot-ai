package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.dto.RepositoryResponse;

import java.util.List;

public interface RepositoryService {
    List<RepositoryResponse> findAll();
    RepositoryResponse getRepositoryById(Long repositoryId);
    void deleteRepository(Long repositoryId);
}
