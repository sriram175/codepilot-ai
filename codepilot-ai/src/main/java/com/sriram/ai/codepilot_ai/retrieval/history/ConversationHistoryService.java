package com.sriram.ai.codepilot_ai.retrieval.history;

import com.sriram.ai.codepilot_ai.entity.Message;

import java.util.List;

public interface ConversationHistoryService {
    String buildConversationHistory(List<Message> messages);
}
