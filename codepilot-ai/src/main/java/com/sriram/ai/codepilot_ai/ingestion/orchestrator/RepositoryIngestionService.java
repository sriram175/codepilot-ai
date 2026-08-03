package com.sriram.ai.codepilot_ai.ingestion.orchestrator;

import com.sriram.ai.codepilot_ai.dto.CloneRequest;

public interface RepositoryIngestionService {
    void ingest(CloneRequest repositoryUrl);
}
