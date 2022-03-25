package com.company.db;

import com.company.model.Chat;
import com.company.model.Hobby;
import com.company.model.User;

import java.util.ArrayList;
import java.util.List;

public class Database {

    public static List<User> customers = new ArrayList<>();
    public static List<Chat> chats = new ArrayList<>();
    public static List<Hobby> hobbies = new ArrayList<>();

    public static void compile(){

        Hobby hobby = new Hobby(1,"\uD83D\uDC65 Do'st ortirish","\uD83D\uDC65 Заводить друзей",
                "\uD83D\uDC65 Make friends");

        Hobby hobby1 = new Hobby(2,"\uD83D\uDDE3 Suhbatlashish", "\uD83D\uDDE3 Разговор",
                "\uD83D\uDDE3 Conversation");

        Hobby hobby2 = new Hobby(3,"\uD83E\uDEC2 Juft izlash", "\uD83E\uDEC2 Поиск пары",
                "\uD83E\uDEC2 Find lover");

        hobbies.add(hobby);
        hobbies.add(hobby1);
        hobbies.add(hobby2);

        // in progress
    }

}
