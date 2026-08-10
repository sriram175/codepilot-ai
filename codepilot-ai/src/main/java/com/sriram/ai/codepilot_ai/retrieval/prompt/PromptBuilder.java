package com.sriram.ai.codepilot_ai.retrieval.prompt;

public interface PromptBuilder {
    String buildChatPrompt(String conversationHistory,
                           String repositoryContext,
                           String question,
                           String repositorySummary);
    String buildQueryRewritePrompt(String conversationHistory,String question);

    String buildArchitecturePrompt(String summary,String conversationHistory,String question);

}
