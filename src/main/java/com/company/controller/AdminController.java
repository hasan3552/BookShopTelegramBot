package com.company.controller;

import com.company.model.User;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
@Setter
public class AdminController extends Thread{
    private Message message;
    private User user;

    public AdminController(Message message,User user) {
        this.message = message;
        this.user = user;
    }

    @Override
    public void run() {

      //  System.out.println("message.getContact() = " + message.getContact());

        // in progress
    }

    public void workCallbackQuery(CallbackQuery callbackQuery, User user) {

    }
}
