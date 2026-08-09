package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryRewriteServiceImpl implements QueryRewriteService{

    private final ChatClient chatClient;
    @Override
    public String rewrite(List<Message> history, String question) {
        String conversationHistory = history.stream()
                .map(message -> message.getMessageRole().name()+":\n"+
                        message.getContent())
                .collect(Collectors.joining("\n\n"));
        String prompt = """
                    You are generating search queries for a code Retrieval-Augmented Generation (RAG) system.
                    
                    Your goal is to rewrite the user's latest question into a standalone search query that will retrieve the most relevant source code.
                    
                    Rules:
                    - Preserve the user's intent.
                    - Resolve references such as "it", "that", "this", "the method", "the repository", etc.
                    - Include the relevant class names, interface names, method names, or component names from the conversation whenever possible.
                    - Make the rewritten question as specific as possible for semantic code search.
                    - Do NOT answer the question.
                    - Return ONLY the rewritten question.
                    - Do not use markdown or explanations.
                    
                    Conversation History:
                    %s
                    
                    Current Question:
                    %s
                    """.formatted(conversationHistory, question);
        return chatClient.prompt(prompt)
                .call()
                .content()
                .trim();
    }
}
