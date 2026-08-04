package com.sriram.ai.codepilot_ai.ingestion.Qdrant;

import com.sriram.ai.codepilot_ai.dto.EmbeddedDocument;
import io.qdrant.client.QdrantClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.qdrant.client.grpc.Collections.Distance;
import io.qdrant.client.grpc.Collections.VectorParams;

import java.util.Map;
import java.util.UUID;
import io.qdrant.client.grpc.Points.PointStruct;
import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;
import io.qdrant.client.grpc.Points.Filter;
import static io.qdrant.client.ConditionFactory.match;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class QdrantServiceImpl implements QdrantService {
    private static final Logger log = LoggerFactory.getLogger(QdrantServiceImpl.class);
    private final QdrantClient qdrantClient;

    @Value("${spring.ai.vectorstore.qdrant.collection-name}")
    private String collectionName;



    @Override
    public void store(List<EmbeddedDocument> embeddedDocuments) {
        if(embeddedDocuments.isEmpty()){
            return ;
        }
        int dimension = embeddedDocuments
                .get(0)
                .getEmbedding()
                .size();

        createCollectionIfNotExists(dimension);
        List<PointStruct> points = embeddedDocuments.stream()
                .map(this::buildPoint)
                .toList();
        try {
            qdrantClient.upsertAsync(collectionName, points).get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to store vectors in Qdrant", e);
        }
        log.info("Stored {} vectors in Qdrant", points.size());
    }

    private void createCollectionIfNotExists(int dimension) {

        try {

            boolean exists = qdrantClient
                    .collectionExistsAsync(collectionName)
                    .get();

            if (exists) {
                return;
            }

            qdrantClient.createCollectionAsync(
                    collectionName,
                    VectorParams.newBuilder()
                            .setSize(dimension)
                            .setDistance(Distance.Cosine)
                            .build()
            ).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Collection {} created", collectionName);
    }


private PointStruct buildPoint(EmbeddedDocument embeddedDocument) {
    Document document = embeddedDocument.getDocument();
    return PointStruct.newBuilder()
            .setId(id(UUID.randomUUID()))
            .setVectors(vectors(embeddedDocument.getEmbedding()))
            .putAllPayload(
                    Map.of(
                            "fileName", value(
                                    document.getMetadata()
                                            .get("fileName")
                                            .toString()
                            ),
                            "filePath", value(
                                    document.getMetadata()
                                            .get("filePath")
                                            .toString()
                            ),
                            "text", value(
                                    document.getText()
                            ),
                            "repositoryId", value((Long)
                                    document.getMetadata()
                                            .get("repositoryId")
                            )
                    )
            )
            .build();
    }
    @Override
    public void deleteRepositoryVector(Long repositoryId){

        try{
            boolean exists = qdrantClient
                    .collectionExistsAsync(collectionName)
                    .get();

            if (!exists) {
                log.info("Collection {} does not exist. Skipping delete.", collectionName);
                return;
            }
            Filter filter = Filter.newBuilder()
                    .addMust(match("repositoryId", repositoryId.longValue()))
                    .build();
            var response = qdrantClient.deleteAsync(collectionName, filter).get();

            log.info("Delete response: {}", response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete repository vector" ,e);
        }
    }

//private UUID generateUUID(EmbeddedDocument embeddedDocument) {
//        Document document = embeddedDocument.getDocument();
//        Long repositoryId = (Long) document.getMetadata().get("repositoryId");
//        String filePath = document.getMetadata().get("filePath").toString();
//        int chunkIndex = embeddedDocument.getChunkIndex();
//        String key = repositoryId + ":" + filePath + ":" + chunkIndex;
//        return UUID.nameUUIDFromBytes(key.getBytes());
//}

}
