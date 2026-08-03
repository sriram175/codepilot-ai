package com.sriram.ai.codepilot_ai.ingestion.Qdrant;

import com.sriram.ai.codepilot_ai.dto.EmbeddedDocument;

import java.util.List;

public interface QdrantService {

    void store(List<EmbeddedDocument> embeddedDocuments);

}