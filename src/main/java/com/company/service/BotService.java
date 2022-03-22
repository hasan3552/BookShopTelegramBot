package com.company.service;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
@Setter
public class BotService extends Thread{

    private Message message;

    public BotService(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        // in progress
    }
}
