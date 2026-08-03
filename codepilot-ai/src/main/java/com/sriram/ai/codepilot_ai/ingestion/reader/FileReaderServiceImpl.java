package com.sriram.ai.codepilot_ai.ingestion.reader;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileReaderServiceImpl implements FileReaderService {

    @Override
    public String read(Path file) {
        try{
            return Files.readString(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
