package com.company.controller;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.Language;
import com.company.enums.UserStatus;
import com.company.model.Chat;
import com.company.model.User;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;
import java.util.regex.Pattern;

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

        if (user.getStatus().equals(UserStatus.ADMIN_WRITE_RESPONSE)){

            Optional<Chat> optional = Database.chats.stream()
                    .filter(chat -> chat.getText() == null && !chat.getIsSending() && chat.getFromId
                            ().equals(user.getId())).findAny();


            if (optional.isPresent()){

                Chat chat = optional.get();
                chat.setIsSending(true);
                user.setStatus(UserStatus.ADMIN_MENU);
                chat.setText(message.getText());

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(chat.getToId()));
                sendMessage.setText("<b>RESPONSE FROM ADMIN !!!</b>\n\n" +message.getText());
                sendMessage.setParseMode(ParseMode.HTML);

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

                SendMessage sendMessage1 = new SendMessage();
                sendMessage1.setText(user.getLanguage().equals(Language.UZ) ? "Sizning javobingiz uzatildi." :
                        user.getLanguage().equals(Language.RU) ? "Ваш ответ отправлен." : "Your reply has been forwarded.");
                sendMessage1.setChatId(String.valueOf(user.getId()));

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);
            }
        }
        // in progress
    }

    public void workCallbackQuery(CallbackQuery callbackQuery, User user) {

        String data = callbackQuery.getData();
        Language language = user.getLanguage();

        if (Pattern.matches("[0-9]{10}/[0-9]{10}",data)){
            String[] split = data.split("/");

            System.out.println("split[0] = " + split[0]);
            System.out.println("split[1] = " + split[1]);

            Chat chat = new Chat(Long.parseLong(split[1]),Long.parseLong(split[0]),null);
            Database.chats.add(chat);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(language.equals(Language.UZ) ? "SHIKOYATGA JAVOB QOLDIRINNG" : language.equals(Language.RU) ?
                    "ОСТАВЬТЕ ОТВЕТ НА ЖАЛОБУ" : "LEAVE AN ANSWER TO THE COMPLAINT");
            sendMessage.setChatId(String.valueOf(user.getId()));

            user.setStatus(UserStatus.ADMIN_WRITE_RESPONSE);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

//            Optional<User> optional = Database.customers.stream()
//                    .filter(user1 -> user1.getId().equals(Long.parseLong(split[0]))).findAny();
//
//            Language language = optional.get().getLanguage();
        }

    }
}
