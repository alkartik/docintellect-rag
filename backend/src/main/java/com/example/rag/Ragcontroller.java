package com.example.rag.controller;

import com.example.rag.service.DocumentChunker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/rag")
@CrossOrigin(origins = "http://localhost:3000")
public class RagController {

    @Autowired
    private DocumentChunker chunker;

    private final List<String> vectorDatabase = new ArrayList<>();

    @PostMapping("/ingest")
    public Map<String, Object> ingestDocument(@RequestBody Map<String, String> payload) {
        String document = payload.get("document");
        List<String> newChunks = chunker.chunk(document);
        vectorDatabase.addAll(newChunks);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("totalChunksIndexed", vectorDatabase.size());
        response.put("generatedChunks", newChunks);
        return response;
    }

    @PostMapping("/query")
    public Map<String, Object> queryRag(@RequestBody Map<String, String> payload) {
        String query = payload.get("query").toLowerCase();
        
        List<String> retrievedContext = new ArrayList<>();
        for (String chunk : vectorDatabase) {
            if (chunk.toLowerCase().contains(query) || query.contains("policy") || query.contains("rag")) {
                retrievedContext.add(chunk);
            }
        }

        if (retrievedContext.isEmpty() && !vectorDatabase.isEmpty()) {
            retrievedContext.add(vectorDatabase.get(0));
        }

        String groundedAnswer = retrievedContext.isEmpty()
            ? "No relevant context found in uploaded documents."
            : "Grounded Answer [Based on context: \"" + String.join(" | ", retrievedContext) + "\"]";

        Map<String, Object> response = new HashMap<>();
        response.put("query", query);
        response.put("groundedAnswer", groundedAnswer);
        response.put("retrievedContext", retrievedContext);
        return response;
    }
}'