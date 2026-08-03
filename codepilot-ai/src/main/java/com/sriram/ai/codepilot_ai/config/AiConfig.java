package com.sriram.ai.codepilot_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Bean
    public TextSplitter textSplitter() {
        return TokenTextSplitter.builder().build();
    }

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
