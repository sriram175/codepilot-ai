package com.sriram.ai.codepilot_ai.ingestion.git;


import org.springframework.beans.factory.annotation.Value;
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
    @Value("${codepilot.repository.root-path}")
    private String repositoryRoot;
    @Override
    public Path cloneRepository(String repositoryUrl) {
        String repositoryName = getRepositoryName(repositoryUrl);

        File directory = new File(repositoryRoot+repositoryName);
        try{

            if(directory.exists()){
                log.info("Repository {} already exists. Skipping clone.", repositoryName);
                return directory.toPath();
            }
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "git",
                    "clone",
                    repositoryUrl,
                    directory.getAbsolutePath()
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            System.out.println("Clone finished");

            Thread.sleep(10000);   // <-- add this temporarily

            if (exitCode != 0) {
                throw new RuntimeException("Failed to clone repository");
            }
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to clone repository", e);
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

    @Override
    public Path pullRepository(String repositoryUrl) {

        String repositoryName = getRepositoryName(repositoryUrl);

        File directory = new File(repositoryRoot, repositoryName);

        if (!directory.exists()) {
            throw new RuntimeException("Repository does not exist locally.");
        }

        try {

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "git",
                    "-C",
                    directory.getAbsolutePath(),
                    "pull"
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Failed to pull repository.");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to pull repository.", e);
        }
        return directory.toPath();
    }

    public  void deleteRepository(String repositoryUrl) {
        String repositoryName = getRepositoryName(repositoryUrl);
        Path repositoryPath = Path.of(repositoryRoot, repositoryName);

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

    @Override
    public boolean repositoryExists(String repositoryUrl) {

        String repositoryName = getRepositoryName(repositoryUrl);

        File directory = new File(repositoryRoot, repositoryName);

        return directory.exists();
    }
}
