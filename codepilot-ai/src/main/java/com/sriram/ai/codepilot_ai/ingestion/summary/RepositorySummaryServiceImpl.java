package com.sriram.ai.codepilot_ai.ingestion.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class RepositorySummaryServiceImpl implements RepositorySummaryService{

    private final ChatClient chatClient;
    private final RepositorySnapshotBuilderService repositorySnapshotBuilderService;
    @Override
    public String generateRepositorySummary(Path repositoryPath) {
        String snapshot = repositorySnapshotBuilderService.buildSnapshot(repositoryPath);
        String prompt = """
                    You are a senior software architect.
                    
                    Analyze the repository snapshot and generate a professional repository summary.
                    
                    Requirements:
                    - Use ONLY the information present in the snapshot.
                    - Do not invent classes, methods, or technologies.
                    - If information is missing, simply omit that section.
                    - Write in clear Markdown.
                    
                    Include these sections:
                    
                    # Project Overview
                    # Purpose
                    # Tech Stack
                    # Main Modules
                    # Architecture
                    # Key Components
                    # Data Flow
                    # External Integrations
                    # Suggestions for Improvement
                    
                    Repository Snapshot:
                    
                    %s
                    """.formatted(snapshot);
        try {
            return chatClient.prompt(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate repository summary", e);
        }
    }
}
