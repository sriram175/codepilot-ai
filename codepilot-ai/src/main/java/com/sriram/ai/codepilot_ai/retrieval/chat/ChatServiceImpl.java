package com.sriram.ai.codepilot_ai.retrieval.chat;

import com.sriram.ai.codepilot_ai.ingestion.Qdrant.QdrantServiceImpl;
import com.sriram.ai.codepilot_ai.retrieval.history.ConversationHistoryService;
import com.sriram.ai.codepilot_ai.retrieval.prompt.PromptBuilder;
import com.sriram.ai.codepilot_ai.service.MessageService;
import com.sriram.ai.codepilot_ai.service.QueryRewriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.sriram.ai.codepilot_ai.dto.ChatResponse;
import com.sriram.ai.codepilot_ai.dto.SearchResultDto;
import com.sriram.ai.codepilot_ai.dto.SourceDto;
import com.sriram.ai.codepilot_ai.entity.Conversation;
import com.sriram.ai.codepilot_ai.entity.Message;
import com.sriram.ai.codepilot_ai.entity.MessageRole;
import com.sriram.ai.codepilot_ai.entity.Repository;
import com.sriram.ai.codepilot_ai.repository.ConversationRepository;
import com.sriram.ai.codepilot_ai.repository.MessageRepository;
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

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
    private final SearchService searchService;
    private final ChatClient chatClient;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final QueryRewriteService queryRewriteService;
    private final ConversationHistoryService conversationHistoryService;
    private final PromptBuilder promptBuilder;
    private MessageService messageService;

    @Value("${codepilot.search.score-threshold}")
    private double scoreThreshold;

    @Override
    public ChatResponse chat(Long conversationId, String question) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        Repository repository = conversation.getRepository();

        messageService.saveUserMessage(conversation,question);
        List<Message> messages = messageService.getConversationMessages(conversationId);

        String conversationHistory = conversationHistoryService.buildConversationHistory(messages);
        String rewrittenQuestion = queryRewriteService.rewrite(messages,question);
        log.info("Original Question : {}", question);
        log.info("Rewritten Question: {}", rewrittenQuestion);
        SearchResultDto searchResultDto = searchService.search(repository.getId(),rewrittenQuestion);
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
        String prompt = promptBuilder.buildChatPrompt(conversationHistory,context,question);
        try {
            String answer = chatClient.prompt(prompt)
                    .call()
                    .content();
            messageService.saveAssistantMessage(conversation,question);
            conversationRepository.save(conversation);
            return ChatResponse.builder()
                    .answer(answer)
                    .sources(sources)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate answer", e);
        }
    }
}
