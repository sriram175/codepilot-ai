package com.sriram.ai.codepilot_ai.ingestion.git;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public class GitCloneServiceImpl implements GitCloneService {

    @Override
    public Path cloneRepository(String repositoryUrl) {
        String repositoryName = getRepositoryName(repositoryUrl);

        File directory = new File("repositories/"+repositoryName);
        try{

            if(directory.exists()){
                System.out.println("Repository already exists. Skipping clone.");
                return directory.toPath();
            }
            Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(directory)
                    .call();
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
}
