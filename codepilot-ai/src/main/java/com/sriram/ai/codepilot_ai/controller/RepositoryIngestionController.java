package com.sriram.ai.codepilot_ai.controller;

import com.sriram.ai.codepilot_ai.dto.CloneRequest;
import com.sriram.ai.codepilot_ai.ingestion.orchestrator.RepositoryIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingest")
public class RepositoryIngestionController {
    private final RepositoryIngestionService repositoryIngestionService;

    @PostMapping
    public String ingestRepository(@RequestBody CloneRequest cloneRequest) {
        repositoryIngestionService.ingest(cloneRequest);
        return "Ingestion started for repository: " + cloneRequest.getRepositoryUrl();
    }
}
