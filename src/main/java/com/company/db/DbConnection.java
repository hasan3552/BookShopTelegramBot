package com.company.db;

import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.UserStatus;
import com.company.model.Chat;
import com.company.model.Follower;
import com.company.model.Hobby;
import com.company.model.User;

import java.sql.*;
import java.util.Optional;

public class DbConnection {

    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "hasan";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/only_conversation_bot_db";
    public static Connection connection;

    public static void addCustomer(User user) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement statement = connection.createStatement();

            String ADD_CUSTOMER = "INSERT INTO " +
                    "customer(id, username, phone_number, language, hobby_id, gender, status, role) \nVALUES (" +
                    +user.getId() + ", '" + user.getUsername() + "', null, null, null ,null,'NEW','REGISTER' );";


            int i = statement.executeUpdate(ADD_CUSTOMER);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void addChat(Chat chat) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement statement = connection.createStatement();

            String ADD_CHAT = "INSERT INTO chat(id, from_id, to_id) \nVALUES (" +
                     chat.getChatId()+","+chat.getFromId()+","+chat.getToId()+" );";


            int i = statement.executeUpdate(ADD_CHAT);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addFollower(Follower follower) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement statement = connection.createStatement();

            String ADD_CHAT = "INSERT INTO follower(id, to_id, with_id) \nVALUES (" +
                    follower.getId()+","+follower.getToId()+","+follower.getWithId()+" );";


            int i = statement.executeUpdate(ADD_CHAT);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readFromDatabase() {

        String readFromCustomer = "SELECT * FROM customer ORDER BY id";
        String readFromHobby = "SELECT * FROM hobby ORDER BY id";
        String readFromChat = "SELECT * FROM chat ORDER BY id";
        String readFromFollower = "SELECT * FROM follower ORDER  BY id";

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet3 = statement.executeQuery(readFromFollower);

            while (resultSet3.next()) {

                int id = resultSet3.getInt("id");
                long toId = resultSet3.getLong("to_id");
                long withId = resultSet3.getLong("with_id");
                Timestamp when = resultSet3.getTimestamp("when");
                String nickname = resultSet3.getString("nickname");
                boolean isBlocked = resultSet3.getBoolean("is_blocked");
                boolean isDeleted = resultSet3.getBoolean("is_deleted");

                Follower follower = new Follower(id, toId, withId, when.toLocalDateTime(),
                                                    nickname, isBlocked, isDeleted);
                Database.followers.add(follower);

            }


            ResultSet resultSet2 = statement.executeQuery(readFromChat);

            while (resultSet2.next()) {

                long id = resultSet2.getLong("id");
                long fromId = resultSet2.getLong("from_id");
                long toId = resultSet2.getLong("to_id");
                String text = resultSet2.getString("chat_text");
                boolean isSending = resultSet2.getBoolean("is_sending");
                boolean isDeleted = resultSet2.getBoolean("is_deleted");

                Chat chat = new Chat(id, fromId, toId, text, isDeleted, isSending);
                Database.chats.add(chat);

            }


            ResultSet resultSet = statement.executeQuery(readFromCustomer);

            while (resultSet.next()) {

                long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String phoneNumber = resultSet.getString("phone_number");
                String language = resultSet.getString("language");

                Optional<Language> optional = Database.languages.stream()
                        .filter(language1 -> language1.name().equals(language))
                        .findAny();

                Language language2 = null;
                if (optional.isPresent()) {
                    language2 = optional.get();
                }

                int hobbyId = resultSet.getInt("hobby_id");
                String gender = resultSet.getString("gender");
                boolean isBlocked = resultSet.getBoolean("is_blocked");

                String status = resultSet.getString("status");
                UserStatus status1 = null;
                Optional<UserStatus> optional1 = Database.userStatuses.stream()
                        .filter(userStatus -> userStatus.name().equals(status))
                        .findAny();

                if (optional1.isPresent()) {
                    status1 = optional1.get();
                }

                String role = resultSet.getString("role");
                Role role2 = null;
                Optional<Role> optional2 = Database.roles.stream()
                        .filter(role1 -> role1.name().equals(role))
                        .findAny();

                if (optional2.isPresent()) {
                    role2 = optional2.get();
                }

                User user = new User(id, username, phoneNumber, language2, hobbyId, gender, isBlocked, status1, role2);
                Database.customers.add(user);

            }

            ResultSet resultSet1 = statement.executeQuery(readFromHobby);

            while (resultSet1.next()) {

                int id = resultSet1.getInt("id");
                String nameUz = resultSet1.getString("name_uz");
                String nameRu = resultSet1.getString("name_ru");
                String nameEn = resultSet1.getString("name_en");
                boolean isDeleted = resultSet1.getBoolean("is_deleted");

                Hobby hobby = new Hobby(id, nameUz, nameRu, nameEn, isDeleted);
                Database.hobbies.add(hobby);

            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void updateCustomerStatus(UserStatus userStatus, Long id) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement statement = connection.createStatement()) {

            String UPDATE_USER_STATUS = "UPDATE customer SET status = '" + userStatus.name() + "' WHERE id = " + id + ";";

            int i = statement.executeUpdate(UPDATE_USER_STATUS);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void updateCustomerPhoneNumber(String phoneNumber, Long id) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement statement = connection.createStatement()) {

            String UPDATE_USER_PHONE = "UPDATE customer SET phone_number = '" + phoneNumber + "' WHERE id = " + id + ";";

            int i = statement.executeUpdate(UPDATE_USER_PHONE);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCustomerLanguage(String language, Long id) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try (Statement statement = connection.createStatement()) {

            String UPDATE_USER_LANGUAGE = "UPDATE customer SET language = '" + language + "' WHERE id = " + id + ";";
            int i = statement.executeUpdate(UPDATE_USER_LANGUAGE);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCustomerHobby(int hobbyId, Long id) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try (Statement statement = connection.createStatement()) {

            String UPDATE_USER_HOBBY_ID = "UPDATE customer SET hobby_id = " + hobbyId + " WHERE id = " + id + ";";
            int i = statement.executeUpdate(UPDATE_USER_HOBBY_ID);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCustomerRole(Role role, Long id) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement statement = connection.createStatement()) {

            String UPDATE_USER_ROLE = "UPDATE customer SET  role = '" + role.name() + "' WHERE id = " + id + ";";
            int i = statement.executeUpdate(UPDATE_USER_ROLE);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateChatTextAndSending(Long chatId, String text) {


        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement statement = connection.createStatement()) {

            String UPDATE_CHAT_TEXT_IS_SENDING = "UPDATE chat SET  chat_text = '" + text + "',is_sending = true" +
                    " WHERE id = " + chatId + ";";
            int i = statement.executeUpdate(UPDATE_CHAT_TEXT_IS_SENDING);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateFollowerNickname(Integer id, String nickname) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement statement = connection.createStatement()) {

            String UPDATE_FOLLOWER = "UPDATE follower SET  nickname = '" + nickname + "' WHERE id = " + id+ ";";
            int i = statement.executeUpdate(UPDATE_FOLLOWER);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
