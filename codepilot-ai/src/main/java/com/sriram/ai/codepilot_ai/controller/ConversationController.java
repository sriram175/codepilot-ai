package com.sriram.ai.codepilot_ai.controller;

import com.sriram.ai.codepilot_ai.dto.ConversationResponse;
import com.sriram.ai.codepilot_ai.dto.CreateConversationResponse;
import com.sriram.ai.codepilot_ai.dto.MessageResponse;
import com.sriram.ai.codepilot_ai.dto.UpdateConversationTitleRequest;
import com.sriram.ai.codepilot_ai.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repositories")
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/{repositoryId}/conversations")
    public ResponseEntity<CreateConversationResponse> createConversation(@PathVariable Long repositoryId){
        return ResponseEntity.ok(conversationService.createConversation(repositoryId));
    }

    @GetMapping("/{repositoryId}/conversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(@PathVariable Long repositoryId){
        return ResponseEntity.ok(conversationService.getConversations(repositoryId));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long conversationId){
        return ResponseEntity.ok(conversationService.getMessages(conversationId));
    }

    @PatchMapping("/conversations/{conversationId}/title")
    public ResponseEntity<Void> updateTitle(
            @PathVariable Long conversationId,
            @RequestBody UpdateConversationTitleRequest request) {

        conversationService.updateTitle(
                conversationId,
                request.getTitle());

        return ResponseEntity.ok().build();
    }
}


