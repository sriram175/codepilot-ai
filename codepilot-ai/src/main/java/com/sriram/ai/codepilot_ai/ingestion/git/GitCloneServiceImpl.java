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

    private String getRepositoryName(String repositoryUrl) {

        String repositoryName = repositoryUrl.substring(repositoryUrl.lastIndexOf("/") + 1);

        if (repositoryName.endsWith(".git")) {
            repositoryName = repositoryName.substring(0, repositoryName.length() - 4);
        }

        return repositoryName;
    }

    public  void deleteRepository(String repositoryUrl) {
        String repositoryName = getRepositoryName(repositoryUrl);
        Path repositoryPath = Path.of("repositories", repositoryName);
        if(Files.notExists(repositoryPath)){
            return ;
        }
        try {
            try (var paths = Files.walk(repositoryPath)) {

                paths.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete repository", e);
        }
        log.info("Deleted repository {}", repositoryName);
    }
}
