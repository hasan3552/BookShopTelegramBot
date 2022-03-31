package com.company.db;

import com.company.enums.Language;
import com.company.enums.Role;
import com.company.model.Chat;
import com.company.model.Follower;
import com.company.model.Hobby;
import com.company.model.User;

import java.util.ArrayList;
import java.util.List;

public class Database {

    public static List<User> customers = new ArrayList<>();
    public static List<Chat> chats = new ArrayList<>();
    public static List<Hobby> hobbies = new ArrayList<>();
    public static List<Follower> followers = new ArrayList<>();

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

//        User admin = new User(1666912639L,"hasan1018");
//        admin.setLanguage(Language.UZ);
        //admin.setRole(Role.ADMIN);

//        customers.add(admin);

        // in progress
    }

}
