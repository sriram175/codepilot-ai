package com.sriram.ai.codepilot_ai.retrieval.chat;

import com.sriram.ai.codepilot_ai.dto.ChatResponse;
import com.sriram.ai.codepilot_ai.dto.SourceDto;

import java.util.List;

public interface ChatService {
    ChatResponse chat(Long conversationId, String question);
}
