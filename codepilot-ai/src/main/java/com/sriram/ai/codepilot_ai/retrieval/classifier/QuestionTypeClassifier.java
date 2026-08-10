package com.sriram.ai.codepilot_ai.retrieval.classifier;

public interface QuestionTypeClassifier {
    QuestionType classify(String question);
}
