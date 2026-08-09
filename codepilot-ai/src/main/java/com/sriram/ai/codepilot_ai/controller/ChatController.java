package com.sriram.ai.codepilot_ai.controller;

import com.sriram.ai.codepilot_ai.dto.ChatResponse;
import com.sriram.ai.codepilot_ai.dto.QueryRequest;
import com.sriram.ai.codepilot_ai.retrieval.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    @PostMapping("/conversations/{conversationId}/chat")
    public ChatResponse askQuestion(@Valid @RequestBody QueryRequest queryRequest, @PathVariable Long conversationId) {
        return chatService.chat(conversationId , queryRequest.getQuestion());
    }
}
