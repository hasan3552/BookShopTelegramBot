package com.company.controller;

import com.company.model.User;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Setter
@Getter
public class ProAdminController extends Thread{

    private Message message;
    private User user;

    public ProAdminController(Message message,User user) {
        this.message = message;
        this.user = user;
    }

    @Override
    public void run() {

    // in progress
    }

    public void workCallbackQuery(CallbackQuery callbackQuery, User user) {


    }
}
