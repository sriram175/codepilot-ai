package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.dto.ConversationResponse;
import com.sriram.ai.codepilot_ai.dto.CreateConversationResponse;
import com.sriram.ai.codepilot_ai.dto.MessageResponse;

import java.util.List;

public interface ConversationService {

    CreateConversationResponse createConversation(Long repositoryId);

    List<ConversationResponse> getConversations(Long repositoryId);

    void deleteConversation(Long conversationId);

    List<MessageResponse> getMessages(Long conversationId);

}
