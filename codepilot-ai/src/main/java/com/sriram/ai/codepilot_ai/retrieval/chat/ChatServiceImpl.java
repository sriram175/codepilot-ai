package com.sriram.ai.codepilot_ai.retrieval.chat;

import com.sriram.ai.codepilot_ai.exception.ConversationNotFoundException;
import com.sriram.ai.codepilot_ai.retrieval.classifier.QuestionTypeClassifier;
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
import com.sriram.ai.codepilot_ai.entity.Repository;
import com.sriram.ai.codepilot_ai.repository.ConversationRepository;
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
    private final QueryRewriteService queryRewriteService;
    private final ConversationHistoryService conversationHistoryService;
    private final PromptBuilder promptBuilder;
    private final MessageService messageService;
    private final QuestionTypeClassifier questionTypeClassifier;

    @Value("${codepilot.search.score-threshold}")
    private double scoreThreshold;

    @Override
    public ChatResponse chat(Long conversationId, String question) {

        log.info("Received question for conversation {}", conversationId);

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException(conversationId));
        Repository repository = conversation.getRepository();

        messageService.saveUserMessage(conversation,question);
        List<Message> messages = messageService.getConversationMessages(conversationId);
        String conversationHistory = conversationHistoryService.buildConversationHistory(messages);
        return handleCodeQuestion(question,
                    conversation,
                    conversationHistory,
                    messages,
                    repository);


    }



    private ChatResponse handleCodeQuestion(String question,
                                            Conversation conversation,
                                            String conversationHistory,
                                            List<Message> messages,
                                            Repository repository
                                            ){
        String rewrittenQuestion = question;
        if(!messages.isEmpty()) {
            rewrittenQuestion = queryRewriteService.rewrite(messages, question);
            log.debug("Rewritten query: {}", rewrittenQuestion);
        }
        SearchResultDto searchResultDto = searchService.search(repository.getId(),rewrittenQuestion);
        List<Document> documents = searchResultDto.getDocuments();
        double maxScore = searchResultDto.getMaxScore();

        log.info(
                "Retrieved {} documents. Max score={}",
                documents.size(),
                maxScore
        );

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
        String prompt = promptBuilder.buildChatPrompt(conversationHistory,context,question, repository.getSummary());
        try {
            log.info("Generating AI response");
            String answer = chatClient.prompt(prompt)
                    .call()
                    .content();
            messageService.saveAssistantMessage(conversation,answer);
            return ChatResponse.builder()
                    .answer(answer)
                    .sources(sources)
                    .build();
        } catch (Exception e) {
            log.error("Gemini request failed", e);
            throw new RuntimeException("Failed to generate answer", e);
        }

    }


}

