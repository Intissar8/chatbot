package com.example.chatbot.telegram;

import com.example.chatbot.agents.AIAgent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.api.key}")
    private String telegramBotToken;
    private AIAgent  aiAgent;

    public TelegramBot(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    //this is how the ai agent will subscribe to the api created using the telegram api key
    @PostConstruct//this method will be initiated right after the constructor
    public void registerTelegramBot() {
        try {
            TelegramBotsApi api=new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    //this is to define how the agent will use the telegram api key
    @Override
    public void onUpdateReceived(Update telegramRequest) {
        try {
            //if no message was sent I do nothing
            if (!telegramRequest.hasMessage()) return;
            //else will use the sent message
            String messageText = telegramRequest.getMessage().getText();
            Long chatId = telegramRequest.getMessage().getChatId();
            sendTypingQuestion(chatId);
            //we use the message to ask the ai agent
            String answer = aiAgent.askAgent(messageText);
            sendTextMessage(chatId,answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }


    }
    @Override
    public String getBotUsername() {
        return "IINTISSARAIBot";
    }
    @Override
    public String getBotToken() {
          return telegramBotToken;
    }

    private void sendTextMessage(long chatId,String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId),text);
        //this is the method that sends the message to the telegram app
        execute(sendMessage);
    }

    //this so that it shows that the telegram agent is typing
    private void sendTypingQuestion(long chatId) throws TelegramApiException {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(String.valueOf(chatId));
        sendChatAction.setAction(ActionType.TYPING);
        execute(sendChatAction);
    }
}
