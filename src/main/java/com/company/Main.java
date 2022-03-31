package com.company;

import com.company.controller.BotControl;
import com.company.db.Database;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.*;

public class Main {

    public static BotControl MY_TELEGRAM_BOT;
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "hasan";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    public static Connection connection;

    public static void main(String[] args) {

        try {
            connection =DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            Statement statement = connection.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {

            Main.MY_TELEGRAM_BOT = new BotControl();
            Database.compile();
            headerMethod();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

