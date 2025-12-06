package org.example.ragservice.services;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final InMemoryVectorStore vectorStore;
    private final OpenAiChatModel chatModel;
    private final OpenAiEmbeddingModel embeddingModel;

    public RagService(
            InMemoryVectorStore vectorStore,
            OpenAiChatModel chatModel,
            OpenAiEmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
    }

    public String ask(String query) {
        // Generate embedding for the user query
        float[] queryEmbeddingFloat = embeddingModel.embed(query); // returns float[]
        double[] queryEmbedding = new double[queryEmbeddingFloat.length];
        for (int i = 0; i < queryEmbeddingFloat.length; i++) {
            queryEmbedding[i] = queryEmbeddingFloat[i]; // convert float -> double
        }

        //  Retrieve top 4 most similar documents
        List<Document> docs = vectorStore.similaritySearch(queryEmbedding, 4);

        //  Build context string
        StringBuilder context = new StringBuilder();
        docs.forEach(d -> context.append(d.getText()).append("\n"));

        //  Build RAG prompt
        String promptText = """
            You are a helpful assistant using Retrieval-Augmented Generation.
            Use the following context to answer the question.

            CONTEXT:
            %s

            QUESTION:
            %s

            ANSWER:
            """.formatted(context, query);

        //  Call OpenAI Chat API
        ChatResponse response = chatModel.call(new Prompt(promptText));

        //  Return the answer
        return response.getResult().getOutput().getText();
    }
}
