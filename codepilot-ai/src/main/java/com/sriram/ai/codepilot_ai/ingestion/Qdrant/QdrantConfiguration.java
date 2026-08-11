package com.sriram.ai.codepilot_ai.ingestion.Qdrant;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantConfiguration {

    @Value("${QDRANT_HOST}")
    private String host;

    @Value("${QDRANT_PORT}")
    private int port;

    @Value("${QDRANT_USE_TLS:false}")
    private boolean useTls;

    @Value("${QDRANT_API_KEY:}")
    private String apiKey;

    @Bean
    public QdrantClient qdrantClient() {

        QdrantGrpcClient.Builder builder =
                QdrantGrpcClient.newBuilder(host, port, useTls);

        if (!apiKey.isBlank()) {
            builder.withApiKey(apiKey);
        }

        return new QdrantClient(builder.build());
    }
}