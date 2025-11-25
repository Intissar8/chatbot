package com.example.chatbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@Component//or service
public class AIAgent {
    private ChatClient chatClient; //its with chatClient that we can communicate with any LLM

    public AIAgent(ChatClient.Builder builder, ChatMemory memory) {

        this.chatClient = builder
                .defaultSystem(/*this the prompt given to the LLM to determine its behavior*/"""
                        Vous etes un assistant qui se charge de repondre aux question
                        de l'utilisateur en fonction du context fourni.
                        si aucun contexte n'est fourni, repond avec je ne sais pas                    
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }

    /*  @GetMapping("/chat")
    public String chat(@RequestParam String query) {
        return chatClient.prompt()
                .user(query)//the users question
                .call()//send the question to the LLM in synchronize mode
                .content();//get the response of the LLM
    }*/

    public Flux<String> askAgent( String query) {
        return chatClient.prompt()
                .user(query)//the users question
                .stream()//send the question to the LLM in a_synchronize mode
                .content();//get the response of the LLM
    }
}
