package com.company.controller;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.UserStatus;
import com.company.model.User;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
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
                message.getText().equals(DemoUtil.SETTING_RU) || message.getText().equals(DemoUtil.SETTING_EN))) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Tilni tanlang." : user.getLanguage().equals(Language.RU)
                    ? "Выберите язык." : "Select the language.");
            InlineKeyboardMarkup language = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(language);

            user.setStatus(UserStatus.SETTING);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (user.getStatus().equals(UserStatus.MENU) && (message.getText().equals(DemoUtil.CONTACT_ADMIN_UZ) ||
                message.getText().equals(DemoUtil.CONTACT_ADMIN_RU) || message.getText().equals(DemoUtil.CONTACT_ADMIN_EN))) {

            user.setStatus(UserStatus.CONTACT_ADMIN);
            SendMessage sendMessage = new SendMessage();

            Language language = user.getLanguage();
            sendMessage.setText(language.equals(Language.UZ) ? "Murojaat maqsadini tanlang:" : language.equals(Language.RU) ?
                    "Выберите цель приложения:" : "Select the purpose of the application:");
            ReplyKeyboardMarkup contactAdmin = KeyboardUtil.getContactAdmin(language);
            sendMessage.setReplyMarkup(contactAdmin);

            sendMessage.setChatId(String.valueOf(user.getId()));

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (user.getStatus().equals(UserStatus.CONTACT_ADMIN) && (message.getText().equals(DemoUtil.PHONE_NUM_EN) ||
                message.getText().equals(DemoUtil.PHONE_NUM_RU) || message.getText().equals(DemoUtil.PHONE_NUM_UZ))) {

            user.setStatus(UserStatus.MENU);

            SendContact sendContact = new SendContact(String.valueOf(user.getId()),
                    "+99891 645 35 52", "Hasan");
            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendContact.setReplyMarkup(menu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendContact);

        } else if (user.getStatus().equals(UserStatus.CONTACT_ADMIN) && (message.getText().equals(DemoUtil.COMPLAINT_UZ) ||
                message.getText().equals(DemoUtil.COMPLAINT_RU) || message.getText().equals(DemoUtil.COMPLAINT_EN))) {

            user.setStatus(UserStatus.WRITE_COMPLAINT);
            Language language = user.getLanguage();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Shikoyatingizni yozib qoldiring.\nU albatta adminga " +
                    "yetkaziladi va sizning shikoyatingiz inobatga olinib sizga albatta javob qaytaramiz." : language.equals(Language.RU) ?
                    "Запишите вашу жалобу.\nОна будет доставлена администратору и мы ответим на вашу жалобу." :
                    "Write down your complaint.\nIt will be delivered to the admin and we will respond to your complaint.");
            // ReplyKeyboardMarkup complaint = KeyboardUtil.getComplaint(language);
            // sendMessage.setReplyMarkup(complaint);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (user.getStatus().equals(UserStatus.WRITE_COMPLAINT)) {
            Language language = user.getLanguage();

            if (message.getText().equals(DemoUtil.COMPLAINT_EN) || message.getText().equals(DemoUtil.COMPLAINT_RU) ||
                    message.getText().equals(DemoUtil.COMPLAINT_UZ) || message.getText().equals(DemoUtil.PHONE_NUM_UZ) ||
                    message.getText().equals(DemoUtil.PHONE_NUM_RU) || message.getText().equals(DemoUtil.PHONE_NUM_EN) ||
                    message.getText().equals(DemoUtil.REKLAMA_EN) || message.getText().equals(DemoUtil.REKLAMA_RU) ||
                    message.getText().equals(DemoUtil.REKLAMA_UZ)) {

                SendMessage sendMessage = new SendMessage(String.valueOf(user.getId()), language.equals(Language.UZ) ?
                        "Bu yerga o'z shikoyatingizni qoldirishingiz kerak." : language.equals(Language.RU) ?
                        "Вы должны оставить свою жалобу здесь." : "You have to leave your complaint here.");

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            } else {

                for (User user1 : Database.customers.stream()
                        .filter(user1 -> user1.getRole().equals(Role.ADMIN)).toList()) {

                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("<b>COMPLAINT !!!</b>\n\n" + message.getText());
                    sendMessage.setParseMode(ParseMode.HTML);
                    sendMessage.setChatId(String.valueOf(user1.getId()));

                    InlineKeyboardMarkup forAdminComplaint = KeyboardUtil.getForAdminComplaint(user1, user);
                    sendMessage.setReplyMarkup(forAdminComplaint);

                    Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

                }

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(language.equals(Language.UZ) ? "Shikoyatingiz qabul qilindi. Javobini kuting." :
                        language.equals(Language.RU) ? "Ваша жалоба принята. Дождитесь ответа." :
                                "Your complaint has been accepted. Wait for the answer.");

                ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                sendMessage.setReplyMarkup(menu);

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

                user.setStatus(UserStatus.MENU);

            }
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
                data.equals(DemoUtil.LANG_RU) || data.equals(DemoUtil.LANG_EN))) {

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
