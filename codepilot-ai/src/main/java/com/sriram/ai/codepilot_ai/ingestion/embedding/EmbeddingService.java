package com.sriram.ai.codepilot_ai.ingestion.embedding;

import com.sriram.ai.codepilot_ai.dto.EmbeddedDocument;
import org.springframework.ai.document.Document;

import java.util.List;

public interface EmbeddingService {
    List<EmbeddedDocument> embed(List<Document> document);
    List<Float> embedQuery(String question);
}
