package com.sriram.ai.codepilot_ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.document.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedDocument {

    private Document document;

    private List<Float> embedding;
}
