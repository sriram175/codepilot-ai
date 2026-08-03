package com.sriram.ai.codepilot_ai.ingestion.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChunkingServiceImpl implements ChunkingService {
    private final TextSplitter textSplitter;
    @Override
    public List<Document> chunk(Document document) {
        return textSplitter.apply(List.of(document));
    }
}
