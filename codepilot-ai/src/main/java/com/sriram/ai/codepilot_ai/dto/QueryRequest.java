package com.sriram.ai.codepilot_ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QueryRequest {
    private Long repositoryId;

    @NotBlank
    private String question;
}
