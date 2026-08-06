package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.dto.RepositoryResponse;
import com.sriram.ai.codepilot_ai.dto.RepositorySummaryResponseDto;

import java.util.List;

public interface RepositoryService {
    List<RepositoryResponse> findAll();
    RepositoryResponse getRepositoryById(Long repositoryId);
    void deleteRepository(Long repositoryId);
    RepositorySummaryResponseDto getRepositorySummary(Long repositoryId);
}
