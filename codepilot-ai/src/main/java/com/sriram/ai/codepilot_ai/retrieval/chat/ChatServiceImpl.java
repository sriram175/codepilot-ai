package com.sriram.ai.codepilot_ai.retrieval.chat;

import com.sriram.ai.codepilot_ai.retrieval.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final SearchService searchService;
    private final ChatClient chatClient;
    @Override
    public String chat(String question) {
        List<Document> documents = searchService.search(question);
        if (documents.isEmpty()) {
            return "I couldn't find any relevant information in the repository.";
        }
        String context = documents.stream()
                .map(Document :: getText)
                .collect(Collectors.joining("\n\n"));
        String prompt = """
                        You are an expert Java backend engineer.
                        
                        Answer the user's question ONLY using the repository context provided below.
                        
                        If the answer cannot be found in the context, reply:
                        "I couldn't find that information in the repository."
                        
                        Repository Context:
                        %s
                        
                        Question:
                        %s
                        """.formatted(context, question);
        try {
            return chatClient.prompt(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate answer", e);
        }
    }
}
