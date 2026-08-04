package com.sriram.ai.codepilot_ai.retrieval.search;

import com.sriram.ai.codepilot_ai.ingestion.embedding.EmbeddingService;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.qdrant.client.WithPayloadSelectorFactory.enable;
import io.qdrant.client.grpc.Points.Filter;
import static io.qdrant.client.ConditionFactory.match;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final EmbeddingService embeddingService;
    private final QdrantClient qdrantClient;

    @Value("${spring.ai.vectorstore.qdrant.collection-name}")
    private String collectionName;
    @Override
    public List<Document> search(Long repositoryId,String question) {
        List<Float> questionEmbedding = embeddingService.embedQuery(question);
        Filter filter = Filter.newBuilder()
                .addMust(match("repositoryId", repositoryId))
                .build();
        Points.SearchPoints searchPoints = Points.SearchPoints.newBuilder()
                .setCollectionName(collectionName)
                .addAllVector(questionEmbedding)
                .setLimit(5)
                .setFilter(filter)
                .setWithPayload(enable(true))
                .build();
        List< Points.ScoredPoint> result;
        try{
            result = qdrantClient.searchAsync(searchPoints).get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to search vectors in Qdrant", e);
        }
        return result.stream().map(point -> Document.builder()
                .text(point.getPayloadMap()
                        .get("text")
                        .getStringValue())
                .metadata("fileName", point.getPayloadMap()
                        .get("fileName")
                        .getStringValue())
                .metadata("filePath", point.getPayloadMap()
                        .get("filePath")
                        .getStringValue())
                .build()).toList();
    }
}
