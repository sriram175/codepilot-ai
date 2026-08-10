package com.sriram.ai.codepilot_ai.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConversationResponse {
    private Long id;
    String title;
    LocalDateTime updatedAt;
}
