package org.example.ragservice.services;

public class Document {
    private final String id;
    private final String text;
    private final double[] embedding;

    public Document(String id, String text, double[] embedding) {
        this.id = id;
        this.text = text;
        this.embedding = embedding;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public double[] getEmbedding() { return embedding; }
}
