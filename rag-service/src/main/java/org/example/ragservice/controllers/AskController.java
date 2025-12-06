package org.example.ragservice.controllers;

import org.example.ragservice.services.RagService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

record AskRequest(String chatId, String message) {}
record AskResponse(String answer) {}

@RestController
@RequestMapping("/api")
public class AskController {

    private final RagService ragService;

    public AskController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public AskResponse ask(@RequestBody AskRequest req) {
        String answer = ragService.ask(req.message());
        return new AskResponse(answer);
    }
}