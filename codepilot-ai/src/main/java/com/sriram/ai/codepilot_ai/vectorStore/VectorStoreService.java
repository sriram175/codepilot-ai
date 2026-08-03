package com.sriram.ai.codepilot_ai.vectorStore;

import com.sriram.ai.codepilot_ai.dto.EmbeddedDocument;

import java.util.List;

public interface VectorStoreService {
        void store(List<EmbeddedDocument> documents);
}
