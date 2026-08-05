package com.sriram.ai.codepilot_ai.ingestion.summary;

import com.sriram.ai.codepilot_ai.ingestion.scanner.FileScannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RepositorySnapshotBuilderServiceImpl implements RepositorySnapshotBuilderService{

    private final FileScannerService fileScannerService;
    private final List<SnapshotExtractor> snapshotExtractors;
    @Override
    public String buildSnapshot(Path repositoryPath) {
        List<Path> files = fileScannerService.scan(repositoryPath);
        StringBuilder snapshot = new StringBuilder();
        for(Path file : files){
            snapshotExtractors.stream()
                    .filter(snapshotExtractor -> snapshotExtractor.supports(file))
                    .findFirst()
                    .ifPresent(snapshotExtractor ->
                            snapshot.append(snapshotExtractor.extract(file))
                                    .append("\n\n"));
        }

        return snapshot.toString();
    }
}
