package com.sriram.ai.codepilot_ai.retrieval.prompt;

import org.springframework.stereotype.Service;

@Service
public class PromptBuilderImpl implements PromptBuilder{
    @Override
    public String buildChatPrompt(String conversationHistory, String repositoryContext, String question,String repositorySummary) {
        String prompt = """
                You are an expert Java backend engineer.
                    
                    Use BOTH:
                    
                    1. Repository Summary
                    2. Repository Context
                    
                    Repository Summary provides the high-level architecture.
                    
                    Repository Context provides implementation details.
                    
                    Use the repository summary to understand the project structure and modules.
                    
                    Use the repository context to answer implementation-specific questions.
                    
                    If the answer cannot be found in either, say so.
                    
                    Conversation History:
                    %s
                    
                    Repository Summary:
                    %s
                    
                    Repository Context:
                    %s
                    
                    Current Question:
                    %s
                        """.formatted(conversationHistory, repositorySummary, repositoryContext, question);
        return prompt;
    }

    @Override
    public String buildQueryRewritePrompt(String conversationHistory, String question) {
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
        return prompt;
    }

    @Override
    public String buildArchitecturePrompt(String repositorySummary,String conversationHistory, String question) {
        return """
                You are a senior Java software architect.
                
                Answer the user's question using ONLY the repository summary below.
                
                If the summary does not contain enough information,
                clearly state what information is missing.
                
                Repository Summary:
                %s
                
                Question:
                %s
                """.formatted(repositorySummary, question);
    }
}
