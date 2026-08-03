package com.sriram.ai.codepilot_ai.ingestion.chunk;

import org.springframework.ai.document.Document;

import java.util.List;

public interface ChunkingService {
    List<Document> chunk(Document content);
}
