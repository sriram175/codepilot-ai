package com.sriram.ai.codepilot_ai.ingestion.scanner;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileScannerServiceImpl implements FileScannerService {
    @Override
    public List<Path> scan(Path repository) {
        try(Stream<Path> paths = Files.walk(repository)){
            return paths.filter(this::isIndexable)
                    .toList();
        }
        catch(IOException ioException){
            throw new RuntimeException("Failed to scan repository: " + ioException.getMessage(), ioException);
        }
    }

    private boolean isSupportedFile(Path path) {

        String fileName = path.toString();

        return fileName.endsWith(".java")
                || fileName.endsWith(".properties")
                || fileName.endsWith(".xml")
                || fileName.endsWith(".md");
    }

    private boolean isIndexable(Path path) {

        String pathString = path.toString();

        if (pathString.contains(".git")
                || pathString.contains("target")
                || pathString.contains("build")
                || pathString.contains(".idea")
                || pathString.contains("node_modules")) {

            return false;
        }

        return Files.isRegularFile(path) && isSupportedFile(path);
    }
}
