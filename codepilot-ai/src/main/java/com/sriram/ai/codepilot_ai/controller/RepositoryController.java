package com.sriram.ai.codepilot_ai.controller;

import com.sriram.ai.codepilot_ai.dto.RepositoryResponse;
import com.sriram.ai.codepilot_ai.dto.RepositorySummaryResponseDto;
import com.sriram.ai.codepilot_ai.entity.Repository;
import com.sriram.ai.codepilot_ai.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repositories")
@RequiredArgsConstructor
public class RepositoryController {
    private final RepositoryService repositoryService;

    @GetMapping
    public List<RepositoryResponse> getAllRepositories() {
        return repositoryService.findAll();
    }

    @GetMapping("/{repositoryId}")
    public RepositoryResponse getRepositoryById(@PathVariable Long repositoryId) {
        return repositoryService.getRepositoryById(repositoryId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{repositoryId}")
    public void deleteRepository(@PathVariable Long repositoryId) {
        repositoryService.deleteRepository(repositoryId);
    }

    @GetMapping("/{repositoryId}/summary")
    public RepositorySummaryResponseDto getRepositorySummary(@PathVariable Long repositoryId){
        return repositoryService.getRepositorySummary(repositoryId);
    }
}
