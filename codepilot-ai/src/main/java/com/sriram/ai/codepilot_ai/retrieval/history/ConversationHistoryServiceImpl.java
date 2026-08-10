package com.sriram.ai.codepilot_ai.retrieval.history;

import com.sriram.ai.codepilot_ai.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationHistoryServiceImpl implements ConversationHistoryService{

    @Override
    public String buildConversationHistory(List<Message> messages) {
        return messages.stream()
                .map(message ->
                        """
                        %s:
                        %s
                        """.formatted(
                                message.getMessageRole(),
                                message.getContent()))
                .collect(Collectors.joining("\n"));
    }
}
