package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.dto.ConversationResponse;
import com.sriram.ai.codepilot_ai.dto.CreateConversationResponse;
import com.sriram.ai.codepilot_ai.dto.MessageResponse;
import com.sriram.ai.codepilot_ai.entity.Conversation;
import com.sriram.ai.codepilot_ai.entity.Repository;
import com.sriram.ai.codepilot_ai.exception.RepositoryNotFoundException;
import com.sriram.ai.codepilot_ai.repository.ConversationRepository;
import com.sriram.ai.codepilot_ai.repository.MessageRepository;
import com.sriram.ai.codepilot_ai.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements  ConversationService{

    private final RepositoryRepository repositoryRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;


    @Override
    public CreateConversationResponse createConversation(Long repositoryId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(()->
                        new RepositoryNotFoundException(repositoryId));
        Conversation conversation = Conversation.
                builder().
                repository(repository).
                title("New Chat").
                build();
        conversationRepository.save(conversation);
        return CreateConversationResponse.builder().
                conversationId(conversation.getId()).build();

    }

    @Override
    public List<ConversationResponse> getConversations(Long repositoryId) {
        return conversationRepository.findByRepositoryIdOrderByUpdatedAtDesc(repositoryId).stream().map(conversation ->
                        ConversationResponse.builder().
                                id(conversation.getId()).
                                title(conversation.getTitle()).
                                updatedAt(conversation.getUpdatedAt()).
                                build())
                .toList();
    }

    @Override
    public void deleteConversation(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow();
        conversationRepository.delete(conversation);

    }

    @Override
    public List<MessageResponse> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(message ->
                        MessageResponse.builder()
                                .id(message.getId())
                                .messageRole(message.getMessageRole())
                                .content(message.getContent())
                                .createdAt(message.getCreatedAt())
                                .build()
                        ).toList();
    }

    @Override
    public void updateTitle(Long conversationId, String title) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow();
        conversation.setTitle(title);
        conversationRepository.save(conversation);

    }
}
