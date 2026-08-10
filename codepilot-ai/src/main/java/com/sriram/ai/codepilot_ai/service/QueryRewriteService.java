package com.sriram.ai.codepilot_ai.service;

import com.sriram.ai.codepilot_ai.entity.Message;

import java.util.List;

public interface QueryRewriteService {

    String rewrite(
            List<Message> history,
            String question
    );

}
