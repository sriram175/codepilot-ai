package com.sriram.ai.codepilot_ai.ingestion.orchestrator;

import com.sriram.ai.codepilot_ai.dto.CloneRequest;
import com.sriram.ai.codepilot_ai.dto.EmbeddedDocument;
import com.sriram.ai.codepilot_ai.entity.Repository;
import com.sriram.ai.codepilot_ai.ingestion.Qdrant.QdrantService;
import com.sriram.ai.codepilot_ai.ingestion.chunk.ChunkingService;
import com.sriram.ai.codepilot_ai.ingestion.embedding.EmbeddingService;
import com.sriram.ai.codepilot_ai.ingestion.git.GitCloneService;
import com.sriram.ai.codepilot_ai.ingestion.reader.FileReaderService;
import com.sriram.ai.codepilot_ai.ingestion.scanner.FileScannerService;
import com.sriram.ai.codepilot_ai.ingestion.summary.RepositorySummaryService;
import com.sriram.ai.codepilot_ai.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepositoryIngestionServiceImpl implements RepositoryIngestionService {

    private final GitCloneService gitCloneService;
    private final FileScannerService fileScannerService;
    private final FileReaderService fileReaderService;
    private final ChunkingService chunkingService;
    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;
    private final RepositoryRepository repositoryRepository;
    private final RepositorySummaryService repositorySummaryService;

    @Override
    public void ingest(CloneRequest cloneRequest) {
        String repositoryName = gitCloneService.getRepositoryName(cloneRequest.getRepositoryUrl());
        Path repositoryPath;
        Repository repository = repositoryRepository.findByUrl(cloneRequest.getRepositoryUrl())
                .orElse(null);
        if(repository == null){
            repository = repositoryRepository.save(Repository.builder()
                    .name(repositoryName)
                    .url(cloneRequest.getRepositoryUrl())
                    .createdAt(LocalDateTime.now())
                    .build());
            repositoryPath = gitCloneService.cloneRepository(cloneRequest.getRepositoryUrl());
        }
        else{
            if (gitCloneService.repositoryExists(cloneRequest.getRepositoryUrl())) {
                repositoryPath = gitCloneService.pullRepository(cloneRequest.getRepositoryUrl());
            } else {
                repositoryPath = gitCloneService.cloneRepository(cloneRequest.getRepositoryUrl());
            }

            qdrantService.deleteRepositoryVector(repository.getId());
        }
        List<Path> files = fileScannerService.scan(repositoryPath);

        for(Path file : files) {
            String content = fileReaderService.read(file);
            Document document = Document.builder()
                    .text(content)
                    .metadata("fileName", file.getFileName().toString())
                    .metadata("filePath", file.toString())
                    .metadata("repositoryId", repository.getId())
                    .build();
            List<Document> chunks = chunkingService.chunk(document);
            List<EmbeddedDocument> embeddedDocuments = embeddingService.embed(chunks);
            qdrantService.store(embeddedDocuments);
        }
        String summary = repositorySummaryService.generateRepositorySummary(repositoryPath);
        repository.setSummary(summary);
        repositoryRepository.save(repository);
    }
}
