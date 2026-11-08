package com.example.rag.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentChunker {
    private final int maxChunkSize = 80;
    private final int overlap = 15;

    public List<String> chunk(String text) {
        List<String> chunks = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            return chunks;
        }

        int length = text.length();
        int start = 0;

        while (start < length) {
            int end = Math.min(start + maxChunkSize, length);
            chunks.add(text.substring(start, end).trim());
            if (end == length) break;
            start += (maxChunkSize - overlap);
        }
        return chunks;
    }
}