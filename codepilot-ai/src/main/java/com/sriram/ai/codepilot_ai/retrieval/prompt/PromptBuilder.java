package com.sriram.ai.codepilot_ai.retrieval.prompt;

public interface PromptBuilder {
    String buildChatPrompt(String conversationHistory,
                           String repositoryContext,
                           String question);
    String buildQueryRewritePrompt(String conversationHistory,String question);

}
