package com.sriram.ai.codepilot_ai.ingestion.embedding;


import com.google.genai.Client;
import com.google.genai.types.EmbedContentResponse;
import com.sriram.ai.codepilot_ai.dto.EmbeddedDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleEmbeddingServiceImpl implements EmbeddingService {

    private final Client client;
    @Value("${codepilot.embedding.model}")
    private String embeddingModel;
    private static final Logger log = LoggerFactory.getLogger(GoogleEmbeddingServiceImpl.class);

    public GoogleEmbeddingServiceImpl(
            @Value("${spring.ai.google.genai.api-key}") String apiKey) {

        log.info("Creating Google GenAI Client");

        this.client = Client.builder()
                .apiKey(apiKey)
                .build();

        log.info("Google GenAI Client created successfully");
    }
    @Override
    public List<EmbeddedDocument> embed(List<Document> documents) {
        List<EmbeddedDocument> embeddedDocuments = new ArrayList<>();
        for(int i = 0; i < documents.size(); i++) {
            Document document = documents.get(i);
            EmbedContentResponse response = client.models.embedContent(
                    embeddingModel,
                    document.getText(),
                    null
            );
            List<Float> embedding = response.embeddings()
                    .orElseThrow(() -> new RuntimeException("No embedding returned"))
                    .get(0)
                    .values()
                    .orElseThrow(() -> new RuntimeException("No embedding values returned"));

            embeddedDocuments.add(
                    new EmbeddedDocument(document, embedding)
            );
            log.info("Embedding dimension: {}", embedding.size());
            log.info("First values: {}", embedding.subList(0, Math.min(5, embedding.size())));
        }
        return embeddedDocuments;
    }

    @Override
    public List<Float> embedQuery(String question) {
        EmbedContentResponse response = client.models.embedContent(
                embeddingModel,
                question,
                null
        );
        List<Float> embedding = response.embeddings()
                .orElseThrow(() -> new RuntimeException("No embedding returned"))
                .get(0)
                .values()
                .orElseThrow(() -> new RuntimeException("No embedding values returned"));
        log.info("Question embedding dimension: {}", embedding.size());
        return embedding;
    }
}
