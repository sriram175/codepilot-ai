package com.sriram.ai.codepilot_ai.retrieval.prompt;

import org.springframework.stereotype.Service;

@Service
public class PromptBuilderImpl implements PromptBuilder{
    @Override
    public String buildChatPrompt(String conversationHistory, String repositoryContext, String question) {
        String prompt = """
                        You are an expert Java backend engineer.
                        
                        Answer the user's question ONLY using the repository context below.
                        
                        Rules:
                        - Do not make up classes, methods, or files.
                        - If the answer is not present in the context, reply:
                          "I couldn't find that information in the repository."
                        - When relevant, mention the filenames that support your answer.
                        
                        Conversation History:
                          %s
                          
                          Repository Context:
                          %s
                          
                          Current Question:
                          %s
                        """.formatted(conversationHistory, repositoryContext, question);
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
}
