package com.company.controller;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotControl extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "arzonkitobmagazinnbot";
    }

    @Override
    public String getBotToken() {
        return "5289486128:AAFlBrK8w_Auc2vOj_ayyotPyEmfdNHoyRw";
    }

    @Override
    public void onUpdateReceived(Update update) {

       if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage(String.valueOf(message.getChatId()),message.getText());
            sendMsg(sendMessage);
        }

    }

    public void sendMsg(SendMessage sendMessage){

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
