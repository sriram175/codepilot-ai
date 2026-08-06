package com.sriram.ai.codepilot_ai.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositorySummaryResponseDto {

    private Long repositoryId;
    private String repositoryName;
    private String summary;
}
