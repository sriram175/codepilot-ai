package com.sriram.ai.codepilot_ai.retrieval.chat;

import com.google.api.client.util.Value;
import com.sriram.ai.codepilot_ai.dto.ChatResponse;
import com.sriram.ai.codepilot_ai.dto.SearchResultDto;
import com.sriram.ai.codepilot_ai.dto.SourceDto;
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
    @Value("${codepilot.search.score-threshold}")
    private double scoreThreshold;

    @Override
    public ChatResponse chat(Long id, String question) {
        SearchResultDto searchResultDto = searchService.search(id,question);
        List<Document> documents = searchResultDto.getDocuments();
        double maxScore = searchResultDto.getMaxScore();

        if (documents.isEmpty() || maxScore < scoreThreshold) {
            return ChatResponse.builder()
                    .answer("I couldn't find any relevant information in the repository.")
                    .sources(List.of())
                    .build();
        }
        String context = documents.stream()
                .map(Document :: getText)
                .collect(Collectors.joining("\n\n"));
        List<SourceDto> sources = documents.stream().
                map(document -> SourceDto.builder()
                        .fileName(document.getMetadata().get("fileName").toString())
                        .filePath(document.getMetadata().get("filePath").toString())
                        .build())
                .distinct()
                .toList();
        String prompt = """
                        You are an expert Java backend engineer.
                        
                        Answer the user's question ONLY using the repository context below.
                        
                        Rules:
                        - Do not make up classes, methods, or files.
                        - If the answer is not present in the context, reply:
                          "I couldn't find that information in the repository."
                        - When relevant, mention the filenames that support your answer.
                        
                        Repository Context:
                        %s
                        
                        Question:
                        %s
                        """.formatted(context, question);
        try {
            String answer = chatClient.prompt(prompt)
                    .call()
                    .content();
            return ChatResponse.builder()
                    .answer(answer)
                    .sources(sources)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate answer", e);
        }
    }
}
