package com.sriram.ai.codepilot_ai.ingestion.git;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Service
public class GitCloneServiceImpl implements GitCloneService {
    private static final Logger log =
            LoggerFactory.getLogger(GitCloneServiceImpl.class);
    @Override
    public Path cloneRepository(String repositoryUrl) {
        String repositoryName = getRepositoryName(repositoryUrl);

        File directory = new File("repositories/"+repositoryName);
        try{

            if(directory.exists()){
                log.info("Repository {} already exists. Skipping clone.", repositoryName);
                return directory.toPath();
            }
            try (Git git = Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(directory)
                    .call()) {
            }
        }
        catch(GitAPIException e){
            throw new RuntimeException("Failed to clone repository: " + e.getMessage(), e);
        }
        return directory.toPath();
    }
    @Override
    public String getRepositoryName(String repositoryUrl) {

        String repositoryName = repositoryUrl.substring(repositoryUrl.lastIndexOf("/") + 1);

        if (repositoryName.endsWith(".git")) {
            repositoryName = repositoryName.substring(0, repositoryName.length() - 4);
        }

        return repositoryName;
    }

    public  void deleteRepository(String repositoryUrl) {
        String repositoryName = getRepositoryName(repositoryUrl);
        Path repositoryPath = Path.of("repositories", repositoryName);
        log.info("Trying to delete: {}", repositoryPath.resolve(".git/objects/pack"));

        if(Files.notExists(repositoryPath)){
            return ;
        }
        try {
            Thread.sleep(2000);
            try (var paths = Files.walk(repositoryPath)) {
               List<Path> files = paths.sorted(Comparator.reverseOrder())
                        .toList();
                for (Path file : files) {
                    log.info("Deleting {}", file);
                    Files.delete(file);

                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete repository", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Deleted repository {}", repositoryName);
    }
}
