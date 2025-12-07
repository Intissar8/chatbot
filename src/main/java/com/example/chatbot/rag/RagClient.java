package com.example.chatbot.rag;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RagClient {

    private final RestTemplate restTemplate;
    private final String ragUrl = "http://localhost:8080/api/ask";

    public RagClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String askRag(String message, String chatId) {

        Map<String, String> request = Map.of(
                "message", message,
                "chatId", chatId
        );

        Map response = restTemplate.postForObject(ragUrl, request, Map.class);

        if (response == null || !response.containsKey("answer")) {
            return "RAG service did not respond.";
        }

        return (String) response.get("answer");
    }
}
