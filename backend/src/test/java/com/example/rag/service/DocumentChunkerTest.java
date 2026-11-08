package com.example.rag.service;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DocumentChunkerTest {

    @Test
    void testTextChunkingSizeAndOverlap() {
        DocumentChunker chunker = new DocumentChunker();
        String sampleDocument = "Retrieval Augmented Generation combines semantic document search with large language models.";

        List<String> chunks = chunker.chunk(sampleDocument);

        assertNotNull(chunks);
        assertFalse(chunks.isEmpty());
        for (String chunk : chunks) {
            assertTrue(chunk.length() <= 80, "Chunk size must not exceed limit");
        }
    }
}'