package com.sriram.ai.codepilot_ai.ingestion.git;

import java.nio.file.Path;

public interface GitCloneService {
    Path cloneRepository(String repositoryUrl);
}
