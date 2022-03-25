package com.company.controller;

import com.company.db.Database;
import com.company.enums.Role;
import com.company.model.User;
import com.company.service.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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

                if (user.getRole().equals(Role.ADMIN)) {

                    AdminController adminController = new AdminController(message);
                    adminController.start();

                } else if (user.getRole().equals(Role.PRO_ADMIN)) {

                    ProAdminController proAdminController = new ProAdminController(message);
                    proAdminController.start();

                } else if (user.getRole().equals(Role.CUSTOMER)) {

                    UserController userController = new UserController(message);
                    userController.start();

                } else if (user.getRole().equals(Role.REGISTER)) {

                    UserController userController = new UserController(message);
                    userController.register(user);

                }
            } else {
                User user = new User(message.getChatId(), message.getFrom().getUserName());
                Database.customers.add(user);
                BotService botService = new BotService(message);
                botService.start();

            }


        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();

            Optional<User> optional = Database.customers.stream()
                    .filter(user -> user.getId().equals(callbackQuery.getFrom().getId()))
                    .findAny();
            if (optional.isPresent()) {

                User user = optional.get();

                if (user.getRole().equals(Role.ADMIN)) {

                    AdminController adminController = new AdminController(message);
                    adminController.workCallbackQuery(callbackQuery, user);

                } else if (user.getRole().equals(Role.PRO_ADMIN)) {

                    ProAdminController proAdminController = new ProAdminController(message);
                    proAdminController.workCallbackQuery(callbackQuery, user);

                } else if (user.getRole().equals(Role.CUSTOMER)) {

                    UserController userController = new UserController(message);
                    userController.workCallbackQuery(callbackQuery, user);

                } else if (user.getRole().equals(Role.REGISTER)) {

                    BotService botService = new BotService(message);
                    botService.workCallbackQuery(callbackQuery, user);
                }

            }


            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            sendMsg(deleteMessage);

        }



    }


    public void sendMsg(SendMessage sendMessage) {

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(DeleteMessage deleteMessage) {

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
