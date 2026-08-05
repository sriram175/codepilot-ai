package com.sriram.ai.codepilot_ai.retrieval.search;

import com.sriram.ai.codepilot_ai.dto.SearchResultDto;
import org.springframework.ai.document.Document;

import java.util.List;

public interface SearchService {
    SearchResultDto search(Long repositoryId, String question);
}
