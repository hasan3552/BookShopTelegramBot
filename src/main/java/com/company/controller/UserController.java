package com.company.controller;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
@Setter
public class UserController extends Thread{

    private final Message message;

    public UserController(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        // in progress
    }
}
