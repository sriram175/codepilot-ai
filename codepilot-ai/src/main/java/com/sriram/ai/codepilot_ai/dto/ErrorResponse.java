package com.sriram.ai.codepilot_ai.dto;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
public class  ErrorResponse{
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}

