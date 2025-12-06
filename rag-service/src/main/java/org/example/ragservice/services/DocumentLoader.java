package org.example.ragservice.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.ai.openai.OpenAiEmbeddingModel;

@Component
public class DocumentLoader implements CommandLineRunner {

    private final InMemoryVectorStore vectorStore;
    private final OpenAiEmbeddingModel embeddingModel;

    public DocumentLoader(InMemoryVectorStore vectorStore, OpenAiEmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    @Override
    public void run(String... args) throws Exception {
        // Example documents
        String[] texts = {
                "Spring Boot is a Java framework for building APIs.",
                "Telegram bots can interact with users using HTTP requests.",
                "RAG stands for Retrieval-Augmented Generation.",
                "Spring AI simplifies using OpenAI models in Spring Boot."
        };

        for (int i = 0; i < texts.length; i++) {
            // Generate embeddings (float[] from Spring AI) and convert to double[]
            float[] embeddingFloat = embeddingModel.embed(texts[i]);
            double[] embedding = new double[embeddingFloat.length];
            for (int j = 0; j < embeddingFloat.length; j++) {
                embedding[j] = embeddingFloat[j];
            }

            Document doc = new Document("doc" + i, texts[i], embedding);
            vectorStore.addDocument(doc);
        }

        System.out.println("Documents loaded into in-memory vector store!");
    }
}
