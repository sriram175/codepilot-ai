package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.entity.Message;
import com.sriram.ai.codepilot_ai.retrieval.history.ConversationHistoryService;
import com.sriram.ai.codepilot_ai.retrieval.prompt.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryRewriteServiceImpl implements QueryRewriteService{

    private final ChatClient chatClient;
    private final ConversationHistoryService conversationHistoryService;
    private final PromptBuilder promptBuilder;
    @Override
    public String rewrite(List<Message> history, String question) {
        String conversationHistory = conversationHistoryService.buildConversationHistory(history);
        String prompt = promptBuilder.buildQueryRewritePrompt(conversationHistory,question);
        return chatClient.prompt(prompt)
                .call()
                .content()
                .trim();
    }
}
