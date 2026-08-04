package com.sriram.ai.codepilot_ai.retrieval.search;

import org.springframework.ai.document.Document;

import java.util.List;

public interface SearchService {
    List<Document> search(Long repositoryId, String question);
}
