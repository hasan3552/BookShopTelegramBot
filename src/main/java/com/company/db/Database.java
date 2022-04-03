package com.company.db;

import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.UserStatus;
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
    public static List<Language> languages = new ArrayList<>();
    public static List<Role> roles=  new ArrayList<>();
    public static List<UserStatus> userStatuses = new ArrayList<>();

    public static void compile(){

        languages.add(Language.UZ);
        languages.add(Language.RU);
        languages.add(Language.EN);

        roles.add(Role.REGISTER);
        roles.add(Role.CUSTOMER);
        roles.add(Role.ADMIN);

        userStatuses.add(UserStatus.NEW);
        userStatuses.add(UserStatus.LANGUAGE);
        userStatuses.add(UserStatus.HAS_CONTACT);
        userStatuses.add(UserStatus.HOBBY);
        userStatuses.add(UserStatus.GENDER);
        userStatuses.add(UserStatus.MENU);
        userStatuses.add(UserStatus.SETTING);
        userStatuses.add(UserStatus.CONTACT_ADMIN);
        userStatuses.add(UserStatus.WRITE_COMPLAINT);
        userStatuses.add(UserStatus.SENDING_REKLAMA);
        userStatuses.add(UserStatus.USER_CONVERSATION);
        userStatuses.add(UserStatus.USER_FOLLOWER_BUTTON);
        userStatuses.add(UserStatus.USER_GET_CONTACT);
        userStatuses.add(UserStatus.USER_GET_TELEGRAM_ID);
        userStatuses.add(UserStatus.USER_WROTE_OTHER);
        userStatuses.add(UserStatus.USER_GIVE_NICKNAME);
        userStatuses.add(UserStatus.ADMIN_WRITE_RESPONSE);
        userStatuses.add(UserStatus.ADMIN_MENU);
        userStatuses.add(UserStatus.ADMIN_PUT_REKLAMA);


        // in progress
    }

}
