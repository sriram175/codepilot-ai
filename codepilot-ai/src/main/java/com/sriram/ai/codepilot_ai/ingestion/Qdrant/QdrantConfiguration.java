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

    @Bean
    public QdrantClient qdrantClient() {

        return new QdrantClient(
                QdrantGrpcClient.newBuilder(
                        host,
                        port,
                        false
                ).build()
        );
    }
}