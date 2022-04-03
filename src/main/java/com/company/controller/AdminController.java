package com.company.controller;

import com.company.Main;
import com.company.db.Database;
import com.company.db.DbConnection;
import com.company.enums.Language;
import com.company.enums.UserStatus;
import com.company.model.Chat;
import com.company.model.User;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import com.company.util.WorkWithFile;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Getter
@Setter
public class AdminController extends Thread {
    private Message message;
    private User user;

    public AdminController(Message message, User user) {
        this.message = message;
        this.user = user;
    }

    @Override
    public void run() {

        if (user.getStatus().equals(UserStatus.ADMIN_WRITE_RESPONSE)) {

            Optional<Chat> optional = Database.chats.stream()
                    .filter(chat -> chat.getText() == null && !chat.getIsSending() && chat.getFromId
                            ().equals(user.getId())).findAny();


            if (optional.isPresent()) {

                Chat chat = optional.get();
                chat.setIsSending(true);
                chat.setText(message.getText());
                DbConnection.updateChatTextAndSending(chat.getChatId(),message.getText());

                user.setStatus(UserStatus.ADMIN_MENU);
                DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(chat.getToId()));
                sendMessage.setText("<b>RESPONSE FROM ADMIN !!!</b>\n\n" + message.getText());
                sendMessage.setParseMode(ParseMode.HTML);

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

                SendMessage sendMessage1 = new SendMessage();
                sendMessage1.setText(user.getLanguage().equals(Language.UZ) ? "Sizning javobingiz uzatildi." :
                        user.getLanguage().equals(Language.RU) ? "Ваш ответ отправлен." : "Your reply has been forwarded.");
                sendMessage1.setChatId(String.valueOf(user.getId()));

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);
            }
        } else if (user.getStatus().equals(UserStatus.ADMIN_PUT_REKLAMA)) {

            if (message.hasPhoto()) {
                for (User customer : Database.customers) {

                    user.setStatus(UserStatus.ADMIN_MENU);
                    DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

                    SendPhoto sendPhoto = new SendPhoto();
                    // List<PhotoSize> photo = message.getPhoto();
                    // String fileId = photo.get(photo.size() - 1).getFileId();
                    // InputFile inputFile = new InputFile(eBook1.getUrlPhoto());
                    // sendPhoto.setPhoto(inputFile);

                    List<PhotoSize> photo = message.getPhoto();
                    String fileId = photo.get(photo.size() - 1).getFileId();
                    InputFile inputFile = new InputFile(fileId);
                    sendPhoto.setPhoto(inputFile);
                    sendPhoto.setChatId(String.valueOf(customer.getId()));
                    sendPhoto.setCaption(message.getCaption());

                    Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
                }
            } else {

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Reklama yuborilmadi. Qaytadan urinib ko'ring." :
                        user.getLanguage().equals(Language.RU) ? "Объявление не отправлено. Пожалуйста, попробуйте еще раз." :
                                "The ad was not sent. Please try again.");

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }


        } else if (message.getText().equals("/start")) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText("<b>WELCOME ADMIN !!!</b>");
            sendMessage.setParseMode(ParseMode.HTML);

            ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(user.getLanguage());
            sendMessage.setReplyMarkup(adminMenu);

            user.setStatus(UserStatus.ADMIN_MENU);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(UserStatus.ADMIN_MENU) && (message.getText().equals(DemoUtil.SETTING_UZ) ||
                message.getText().equals(DemoUtil.SETTING_RU) || message.getText().equals(DemoUtil.SETTING_EN))) {

            Language language = user.getLanguage();
            user.setStatus(UserStatus.SETTING);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Tilni tanlang:" : language.equals(Language.RU) ?
                    "Выберите язык:" : "Choose  language:");

            InlineKeyboardMarkup languageIn = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(languageIn);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (user.getStatus().equals(UserStatus.ADMIN_MENU) && (message.getText().equals(DemoUtil.ALL_USERS_UZ) ||
                message.getText().equals(DemoUtil.ALL_USERS_RU) || message.getText().equals(DemoUtil.ALL_USERS_EN))) {

            WorkWithFile workWithFile = new WorkWithFile(message);
            workWithFile.start();

        } else if (user.getStatus().equals(UserStatus.ADMIN_MENU) && (message.getText().equals(DemoUtil.SEND_REKLAMA_UZ) ||
                message.getText().equals(DemoUtil.SEND_REKLAMA_RU) || message.getText().equals(DemoUtil.SEND_REKLAMA_EN))) {

            user.setStatus(UserStatus.ADMIN_PUT_REKLAMA);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ?
                    "Reklamani yuboring.\n\n<b>ESLATMA REKLAMADA RASM BO'LISHI SHART.</b>"
                    : user.getLanguage().equals(Language.RU) ?
                    "Подать объявление.\n\n<b>ВНИМАНИЕ РЕКЛАМА ДОЛЖНА БЫТЬ В ОБЪЯВЛЕНИИ.</b>" :
                    "Submit an ad.\n\n<b>NOTE ADVERTISING MUST BE IN THE ADVERTISEMENT.</b>");
            sendMessage.setParseMode(ParseMode.HTML);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }
        // in progress
    }

    public void workCallbackQuery(CallbackQuery callbackQuery, User user) {

        String data = callbackQuery.getData();
        Language language = user.getLanguage();
        Message message1 = callbackQuery.getMessage();

        if (Pattern.matches("[0-9]{10}/[0-9]{10}", data)) {
            String[] split = data.split("/");

            System.out.println("split[0] = " + split[0]);
            System.out.println("split[1] = " + split[1]);

            Chat chat = new Chat(Long.parseLong(split[1]), Long.parseLong(split[0]), null);
            DbConnection.addChat(chat);
            Database.chats.add(chat);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(language.equals(Language.UZ) ? "SHIKOYATGA JAVOB QOLDIRINNG" : language.equals(Language.RU) ?
                    "ОСТАВЬТЕ ОТВЕТ НА ЖАЛОБУ" : "LEAVE AN ANSWER TO THE COMPLAINT");
            sendMessage.setChatId(String.valueOf(user.getId()));

            user.setStatus(UserStatus.ADMIN_WRITE_RESPONSE);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(UserStatus.SETTING) && (data.equals(DemoUtil.LANG_UZ) ||
                data.equals(DemoUtil.LANG_RU) || data.equals(DemoUtil.LANG_EN))) {
            user.setStatus(UserStatus.ADMIN_MENU);
            user.setLanguage(data.equals(DemoUtil.LANG_UZ) ? Language.UZ : data.equals(DemoUtil.LANG_RU) ?
                    Language.RU : Language.EN);

            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());
            DbConnection.updateCustomerLanguage(user.getLanguage().name(), user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Til o'zgartirildi." : user.getLanguage().equals(Language.RU) ?
                    "Язык изменен." : "Language changed.");
            ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(user.getLanguage());
            sendMessage.setReplyMarkup(adminMenu);
            sendMessage.setChatId(String.valueOf(user.getId()));

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }else if (data.equals(DemoUtil.REKLAMA_SENDING_INLINE)){

            if (message1.hasPhoto()) {
                for (User customer : Database.customers) {

                    user.setStatus(UserStatus.ADMIN_MENU);
                    DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

                    SendPhoto sendPhoto = new SendPhoto();
                    // List<PhotoSize> photo = message.getPhoto();
                    // String fileId = photo.get(photo.size() - 1).getFileId();
                    // InputFile inputFile = new InputFile(eBook1.getUrlPhoto());
                    // sendPhoto.setPhoto(inputFile);

                    List<PhotoSize> photo = message1.getPhoto();
                    String fileId = photo.get(photo.size() - 1).getFileId();
                    InputFile inputFile = new InputFile(fileId);
                    sendPhoto.setPhoto(inputFile);
                    sendPhoto.setChatId(String.valueOf(customer.getId()));
                    sendPhoto.setCaption(message1.getCaption());

                    Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
                }
            }
        }

    }
}
