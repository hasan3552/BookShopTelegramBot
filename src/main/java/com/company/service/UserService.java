package com.company.service;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.Language;
import com.company.enums.UserStatus;
import com.company.model.Follower;
import com.company.model.User;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserService extends Thread {

    private Message message;
    private User user;
    private Language language;

    public UserService(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }


    public void showMyFriends() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        List<User> userList = new ArrayList<>();

        for (Follower follower : Database.followers) {
            for (User customer : Database.customers) {

                if (follower.getToId().equals(user.getId()) && customer.getId().equals(follower.getWithId())){
                    userList.add(customer);
                }

            }
        }

        if (userList.isEmpty()) {
            sendMessage.setText(language.equals(Language.UZ) ? "Sizning do'stlar listlaringiz bo'sh." : language.equals(Language.RU) ?
                    "Ваши списки друзей пусты." : "Your friends lists are empty.");
        } else {

            String text = language.equals(Language.UZ) ? "Sizning obunachilaringiz ro'yhati.\n\n" : language.equals(Language.RU) ?
                    "Список ваших подписчиков.\n\n" : "List of your subscribers.\n\n";

            for (User customer : userList) {

                text = text.concat( customer.getId() + " | " + customer.getPhoneNumber()+"\n");

            }

            sendMessage.setText(text);
        }
        user.setStatus(UserStatus.MENU);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void addFriend() {
        Language language = user.getLanguage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Do'stlashish menyu."  : language.equals(Language.RU) ?
                "Дружелюбное меню." : "Friendship menu.");

        InlineKeyboardMarkup friendShipMenu = KeyboardUtil.getFriendShipMenu(language);
        sendMessage.setReplyMarkup(friendShipMenu);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }
}
