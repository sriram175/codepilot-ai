package com.sriram.ai.codepilot_ai.retrieval.classifier;

import org.springframework.stereotype.Service;

@Service
public class QuestionTypeClassifierImpl implements QuestionTypeClassifier{
    @Override
    public QuestionType classify(String question) {

            String q = question.toLowerCase();

            if (q.contains("architecture")
                    || q.contains("design")
                    || q.contains("overview")
                    || q.contains("summary")
                    || q.contains("module")
                    || q.contains("flow")
                    || q.contains("tech stack")
                    || q.contains("maintainability")
                    || q.contains("refactor")
                    || q.contains("solid")) {

                return QuestionType.ARCHITECTURE;
            }

            return QuestionType.CODE;
    }
}
