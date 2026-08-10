package com.sriram.ai.codepilot_ai.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RepositoryResponse {
    private Long repositoryId;
    private String repositoryName;
    private String repositoryUrl;
    private String repositorySummary;
    private LocalDateTime createdAt;
}
