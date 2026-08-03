package com.sriram.ai.codepilot_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		exclude = {
				org.springframework.ai.vectorstore.qdrant.autoconfigure.QdrantVectorStoreAutoConfiguration.class
		}
)

public class CodepilotAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodepilotAiApplication.class, args);
	}

}
