package org.example.ragservice.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.example.ragservice.utils.PdfReader;
import org.example.ragservice.utils.TextUtils;

import java.io.InputStream;
import java.util.List;

@Component
public class PdfDocumentLoader implements CommandLineRunner {

    private final InMemoryVectorStore vectorStore;
    private final OpenAiEmbeddingModel embeddingModel;

    public PdfDocumentLoader(InMemoryVectorStore vectorStore, OpenAiEmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    @Override
    public void run(String... args) throws Exception {
        //  Load PDF from resources folder (classpath)
        InputStream is = getClass().getClassLoader().getResourceAsStream("sample.pdf");
        if (is == null) {
            throw new RuntimeException("PDF not found in resources!");
        }

        //  Extract text
        String text = PdfReader.extractText(is);

        //  Split text into chunks
        List<String> chunks = TextUtils.splitText(text, 500);

        // Generate embeddings and add to vector store
        for (int i = 0; i < chunks.size(); i++) {
            float[] embeddingFloat = embeddingModel.embed(chunks.get(i));
            double[] embedding = new double[embeddingFloat.length];
            for (int j = 0; j < embeddingFloat.length; j++) {
                embedding[j] = embeddingFloat[j];
            }
            Document doc = new Document("pdf_" + i, chunks.get(i), embedding);
            vectorStore.addDocument(doc);
        }

        System.out.println("PDF loaded into in-memory vector store!");
    }
}
