package com.sriram.ai.codepilot_ai.ingestion.summary;

import java.nio.file.Path;

public interface SnapshotExtractor {
    boolean supports(Path file);
    String extract(Path file);
}
