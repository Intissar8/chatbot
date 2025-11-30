package com.example.chatbot.web;

import com.example.chatbot.agents.AIAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {
    private ChatClient chatClient; //its with chatClient that we can communicate with any LLM
    private AIAgent aiAgent;


    public ChatController(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @GetMapping(value = "/chat",produces = MediaType.TEXT_PLAIN_VALUE/*to get the output without the spelling mistakes aside from the é,à*/)
    public String chat(@RequestParam String query) {
        return aiAgent.askAgent(query);
    }

}
