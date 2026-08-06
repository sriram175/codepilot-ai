package com.sriram.ai.codepilot_ai.ingestion.summary;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@Component
public class PomSnapshotExtractor implements SnapshotExtractor {
    @Override
    public boolean supports(Path file) {
        return file.getFileName().toString().equals("pom.xml");
    }

    @Override
    public String extract(Path file) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(file.toFile());

            StringBuilder sb = new StringBuilder();

            sb.append("File: pom.xml\n");
            sb.append("Build Tool: Maven\n");

            NodeList javaVersions = document.getElementsByTagName("java.version");
            if (javaVersions.getLength() > 0) {
                sb.append("Java Version: ")
                        .append(javaVersions.item(0).getTextContent())
                        .append("\n");
            }

            NodeList parents = document.getElementsByTagName("parent");
            if (parents.getLength() > 0) {

                Element parent = (Element) parents.item(0);

                String artifactId = getText(parent, "artifactId");
                String version = getText(parent, "version");

                sb.append("Parent: ")
                        .append(artifactId)
                        .append(" ")
                        .append(version)
                        .append("\n");
            }

            sb.append("Dependencies:\n");

            NodeList dependencies = document.getElementsByTagName("dependency");

            for (int i = 0; i < dependencies.getLength(); i++) {

                Element dependency = (Element) dependencies.item(i);

                String groupId = getText(dependency, "groupId");
                String artifactId = getText(dependency, "artifactId");

                if (artifactId != null) {
                    sb.append(" - ");

                    if (groupId != null) {
                        sb.append(groupId).append(":");
                    }

                    sb.append(artifactId).append("\n");
                }
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse pom.xml", e);
        }
    }

    private String getText(Element element, String tagName) {

        NodeList nodeList = element.getElementsByTagName(tagName);

        if (nodeList.getLength() == 0) {
            return null;
        }

        return nodeList.item(0).getTextContent();
    }
}
