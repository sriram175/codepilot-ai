package com.sriram.ai.codepilot_ai.dto;

import com.sriram.ai.codepilot_ai.entity.MessageRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponse {
    private Long id;
    private String content;
    private MessageRole messageRole;
    private LocalDateTime createdAt;
}
