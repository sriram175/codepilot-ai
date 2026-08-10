package com.sriram.ai.codepilot_ai.exception;

public class ConversationNotFoundException extends RuntimeException{
    public ConversationNotFoundException(Long conversationId){
        super("Conversation not found with id: "+conversationId);
    }
}
