package com.company.controller;

import com.company.db.Database;
import com.company.enums.Role;
import com.company.model.User;
import com.company.service.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

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
            Optional<User> optional = Database.customers.stream()
                    .filter(user -> user.getId().equals(message.getChatId())).findAny();

            if (optional.isPresent()) {
                User user = optional.get();

                if (user.getRoles().contains(Role.ADMIN)) {

                    AdminController adminController = new AdminController(message);
                    adminController.start();
                } else {
                    UserController userController = new UserController(message);
                    userController.start();
                }

            } else {
                BotService botService = new BotService(message);
                botService.start();
            }
        }


    }

    public void sendMsg(SendMessage sendMessage) {

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
