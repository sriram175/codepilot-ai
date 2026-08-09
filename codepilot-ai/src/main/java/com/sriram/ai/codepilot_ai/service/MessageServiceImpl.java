package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.entity.Conversation;
import com.sriram.ai.codepilot_ai.entity.Message;
import com.sriram.ai.codepilot_ai.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;
    @Override
    public void saveUserMessage(Conversation conversation, String content) {
        messageRepository.save(
                Message.builder()
                        .conversation(conversation)
                        .content(content)
                        .build()
        );
    }

    @Override
    public void saveAssistantMessage(Conversation conversation, String content) {
        messageRepository.save(
                Message.builder()
                        .conversation(conversation)
                        .content(content)
                        .build()
        );
    }

    @Override
    public List<Message> getConversationMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
