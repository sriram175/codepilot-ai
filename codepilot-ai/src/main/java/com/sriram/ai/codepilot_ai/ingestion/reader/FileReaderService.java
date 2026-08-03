package com.sriram.ai.codepilot_ai.ingestion.reader;

import java.nio.file.Path;

public interface FileReaderService {
    String read(Path file);
}
