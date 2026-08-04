package com.sriram.ai.codepilot_ai.controller;

import com.sriram.ai.codepilot_ai.dto.QueryRequest;
import com.sriram.ai.codepilot_ai.retrieval.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    @PostMapping("/ask")
    public String askQuestion(@Valid @RequestBody QueryRequest queryRequest) {
        return chatService.chat(queryRequest.getRepositoryId() , queryRequest.getQuestion());
    }
}
