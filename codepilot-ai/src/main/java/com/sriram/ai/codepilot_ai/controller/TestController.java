package com.sriram.ai.codepilot_ai.controller;

import com.sriram.ai.codepilot_ai.ingestion.git.GitCloneService;
import com.sriram.ai.codepilot_ai.retrieval.classifier.QuestionTypeClassifier;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final GitCloneService gitCloneService;
    private final QuestionTypeClassifier questionTypeClassifier;

    @PostMapping("/clone-delete")
    public String cloneAndDelete() {

        String repositoryUrl = "https://github.com/sriram175/smart-expense-tracker.git";

        gitCloneService.cloneRepository(repositoryUrl);

        gitCloneService.deleteRepository(repositoryUrl);

        return "Done";
    }
    @PostMapping("/classifier")
    public String testClassifier(@RequestParam String question){
        return questionTypeClassifier.classify(question).toString();
    }
}