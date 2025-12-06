package org.example.ragservice.services;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InMemoryVectorStore {

    private final List<Document> documents = new ArrayList<>();

    public void addDocument(Document doc) {
        documents.add(doc);
    }

    public List<Document> similaritySearch(double[] queryEmbedding, int topK) {
        return documents.stream()
                .sorted((d1, d2) ->
                        Double.compare(cosineSimilarity(d2.getEmbedding(), queryEmbedding),
                                cosineSimilarity(d1.getEmbedding(), queryEmbedding))
                )
                .limit(topK)
                .toList();
    }

    private double cosineSimilarity(double[] vec1, double[] vec2) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            normA += vec1[i] * vec1[i];
            normB += vec2[i] * vec2[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-10);
    }
}