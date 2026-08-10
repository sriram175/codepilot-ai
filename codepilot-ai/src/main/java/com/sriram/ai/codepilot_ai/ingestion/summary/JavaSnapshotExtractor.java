package com.sriram.ai.codepilot_ai.ingestion.summary;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class JavaSnapshotExtractor implements SnapshotExtractor{

    private final JavaParser javaParser;

    public JavaSnapshotExtractor() {
        ParserConfiguration configuration = new ParserConfiguration()
                .setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);

        this.javaParser = new JavaParser(configuration);
    }

    @Override
    public boolean supports(Path file) {
        return file.getFileName().toString().endsWith(".java");
    }

    @Override
    public String extract(Path file) {

        try {
            ParseResult<CompilationUnit> result = javaParser.parse(file);

            if (result.getResult().isEmpty()) {
                throw new RuntimeException("Failed to parse: " + file);
            }
            CompilationUnit cu = result.getResult().get();

            StringBuilder sb = new StringBuilder();

            sb.append("File: ").append(file.getFileName()).append("\n");

            cu.getPackageDeclaration()
                    .ifPresent(p -> sb.append("Package: ")
                            .append(p.getNameAsString())
                            .append("\n"));

            cu.getTypes().forEach(type -> {

                if (type.isClassOrInterfaceDeclaration()) {
                    var declaration = type.asClassOrInterfaceDeclaration();

                    sb.append(declaration.isInterface() ? "Interface: " : "Class: ")
                            .append(declaration.getNameAsString())
                            .append("\n");
                    if (!declaration.getImplementedTypes().isEmpty()) {

                        sb.append("Implements:\n");

                        declaration.getImplementedTypes().forEach(implemented ->
                                sb.append(" - ")
                                        .append(implemented.getNameAsString())
                                        .append("\n"));
                    }
                    if (!declaration.getExtendedTypes().isEmpty()) {

                        sb.append("Extends:\n");

                        declaration.getExtendedTypes().forEach(parent ->
                                sb.append(" - ")
                                        .append(parent.getNameAsString())
                                        .append("\n"));
                    }
                }
                if (!type.getAnnotations().isEmpty()) {

                    sb.append("Annotations:\n");

                    type.getAnnotations().forEach(annotation ->
                            sb.append(" - @")
                                    .append(annotation.getNameAsString())
                                    .append("\n"));
                }

                if (!type.getMethods().isEmpty()) {

                    sb.append("Methods:\n");

                    type.getMethods().forEach(method ->
                            sb.append(" - ")
                                    .append(method.getDeclarationAsString(false, false, false))
                                    .append("\n"));
                }
            });

            return sb.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
