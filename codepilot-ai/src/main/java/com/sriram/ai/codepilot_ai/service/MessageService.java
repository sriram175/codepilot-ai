package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.entity.Conversation;
import com.sriram.ai.codepilot_ai.entity.Message;

import java.util.List;

public interface MessageService {
    void saveUserMessage(Conversation conversation, String content);
    void saveAssistantMessage(Conversation conversation, String content);
    List<Message> getConversationMessages(Long conversationId);
}
