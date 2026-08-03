package com.sriram.ai.codepilot_ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ChatModel chatModel;

    @GetMapping
    public String test() {

        return chatModel.call("Hey");
    }
}
