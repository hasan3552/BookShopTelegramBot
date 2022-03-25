package com.company.controller;

import com.company.Main;
import com.company.enums.UserStatus;
import com.company.model.User;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Getter
@Setter
public class UserController extends Thread{

    private  Message message;

    public UserController(Message message) {
        this.message = message;
    }

    @Override
    public void run() {



        // in progress
    }

    public void register(User user) {

        if (message.hasContact() && user.getStatus().equals(UserStatus.NEW) ){

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setText("CHANGE LANGUAGE");
            InlineKeyboardMarkup language = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(language);
            user.setStatus(UserStatus.LANGUAGE);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }

    }

    public void workCallbackQuery(CallbackQuery callbackQuery, User user) {


    }
}
