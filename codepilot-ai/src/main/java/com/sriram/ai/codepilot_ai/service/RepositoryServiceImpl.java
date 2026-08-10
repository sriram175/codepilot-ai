package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.dto.RepositoryResponse;
import com.sriram.ai.codepilot_ai.dto.RepositorySummaryResponseDto;
import com.sriram.ai.codepilot_ai.entity.Repository;
import com.sriram.ai.codepilot_ai.exception.RepositoryNotFoundException;
import com.sriram.ai.codepilot_ai.ingestion.Qdrant.QdrantService;
import com.sriram.ai.codepilot_ai.ingestion.git.GitCloneServiceImpl;
import com.sriram.ai.codepilot_ai.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class RepositoryServiceImpl implements RepositoryService {
    private final RepositoryRepository repositoryRepository;
    private final QdrantService qdrantService;

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
                .orElseThrow(() -> new RepositoryNotFoundException(repositoryId));
        return mapToRepositoryResponse(repository);
    }

    @Override
    public void deleteRepository(Long repositoryId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RepositoryNotFoundException(repositoryId));
        qdrantService.deleteRepositoryVector(repositoryId);
        repositoryRepository.delete(repository);
    }

    @Override
    public RepositorySummaryResponseDto getRepositorySummary(Long repositoryId) {
        Repository repository =  repositoryRepository.findById(repositoryId)
                .orElseThrow(() ->
                        new RepositoryNotFoundException(repositoryId));
        return RepositorySummaryResponseDto.builder()
                .repositoryId(repository.getId())
                .repositoryName(repository.getName())
                .summary(repository.getSummary())
                .build();
    }

    private RepositoryResponse mapToRepositoryResponse(Repository repository) {
        return RepositoryResponse.builder()
                .repositoryId(repository.getId())
                .repositoryName(repository.getName())
                .repositoryUrl(repository.getUrl())
                .repositorySummary(repository.getSummary())
                .createdAt(repository.getCreatedAt())
                .build();
    }
}
