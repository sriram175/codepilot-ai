package com.sriram.ai.codepilot_ai.ingestion.summary;

import java.nio.file.Path;

public interface RepositorySummaryService {
    String generateRepositorySummary(Path repositoryPath);
}
