package com.company;

import com.company.controller.BotControl;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.util.Stack;

public class Main {


    public static BotControl MY_TELEGRAM_BOT;

    public static void main(String[] args) {


        Main.MY_TELEGRAM_BOT = new BotControl();
        headerMethod();
    }

    private static void headerMethod() {

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(MY_TELEGRAM_BOT);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
      // Stack
        }
    }

