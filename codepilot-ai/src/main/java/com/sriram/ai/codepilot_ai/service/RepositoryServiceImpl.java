package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.dto.RepositoryResponse;
import com.sriram.ai.codepilot_ai.entity.Repository;
import com.sriram.ai.codepilot_ai.ingestion.Qdrant.QdrantService;
import com.sriram.ai.codepilot_ai.ingestion.git.GitCloneServiceImpl;
import com.sriram.ai.codepilot_ai.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class RepositoryServiceImpl implements RepositoryService {
    private final RepositoryRepository repositoryRepository;
    private final QdrantService qdrantService;
    private final GitCloneServiceImpl gitCloneServiceImpl;

    Logger log = Logger.getLogger(RepositoryServiceImpl.class.getName());
    @Override
    public List<RepositoryResponse> findAll() {
        List<Repository> repositories = repositoryRepository.findAll();
        return repositories.stream().map(this::mapToRepositoryResponse)
                .toList();
    }

    @Override
    public RepositoryResponse getRepositoryById(Long repositoryId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));
        return mapToRepositoryResponse(repository);
    }

    @Override
    public void deleteRepository(Long repositoryId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));
        qdrantService.deleteRepositoryVector(repositoryId);
//        gitCloneServiceImpl.deleteRepository(repository.getUrl());
        repositoryRepository.delete(repository);
    }

    private RepositoryResponse mapToRepositoryResponse(Repository repository) {
        return RepositoryResponse.builder()
                .repositoryId(repository.getId())
                .repositoryName(repository.getName())
                .repositoryUrl(repository.getUrl())
                .createdAt(repository.getCreatedAt())
                .build();
    }
}
