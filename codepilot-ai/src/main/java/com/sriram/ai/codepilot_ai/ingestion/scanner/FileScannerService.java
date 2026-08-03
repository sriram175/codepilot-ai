package com.sriram.ai.codepilot_ai.ingestion.scanner;

import java.nio.file.Path;
import java.util.List;

public interface FileScannerService {
    List<Path> scan(Path repository);
}
