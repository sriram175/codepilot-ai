package com.sriram.ai.codepilot_ai.runner;

import com.sriram.ai.codepilot_ai.ingestion.git.GitCloneService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitCloneRunner implements CommandLineRunner {
    private final GitCloneService gitCloneService;

    @Override
    public void run(String... args) throws Exception {
//        gitCloneService.cloneRepository("https://github.com/sriram175/smart-expense-tracker.git");
    }
}
