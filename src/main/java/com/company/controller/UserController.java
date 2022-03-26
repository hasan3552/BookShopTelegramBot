package com.company.controller;

import com.company.Main;
import com.company.enums.Language;
import com.company.enums.UserStatus;
import com.company.model.User;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Getter
@Setter
public class UserController extends Thread {

    private Message message;
    private User user;

    public UserController(Message message, User user) {
        this.message = message;
        this.user = user;
    }

    @Override
    public void run() {

        if (user.getStatus().equals(UserStatus.MENU) && (message.getText().equals(DemoUtil.SETTING_UZ) ||
                message.getText().equals(DemoUtil.SETTING_RU) || message.getText().equals(DemoUtil.SETTING_EN))){

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Tilni tanlang." : user.getLanguage().equals(Language.RU)
            ? "Выберите язык." : "Select the language.");
            InlineKeyboardMarkup language = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(language);

            user.setStatus(UserStatus.SETTING);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }


        // in progress
    }

    public void register(User user) {

        if (message.hasContact() && user.getStatus().equals(UserStatus.NEW)) {

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

        String data = callbackQuery.getData();

        if (user.getStatus().equals(UserStatus.SETTING) && (data.equals(DemoUtil.LANG_UZ) ||
                data.equals(DemoUtil.LANG_RU) || data.equals(DemoUtil.LANG_EN))){

            user.setLanguage(data.equals(DemoUtil.LANG_UZ) ? Language.UZ : data.equals(DemoUtil.LANG_RU) ?
            Language.RU : Language.EN);
            user.setStatus(UserStatus.MENU);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendMessage.setReplyMarkup(menu);
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Til o'zgartirildi." : user.getLanguage().equals(Language.RU)
            ? "Язык изменен." : "Language changed.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }


    }
}
