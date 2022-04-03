package com.company.service;

import com.company.Main;
import com.company.db.DbConnection;
import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.UserStatus;
import com.company.model.User;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Getter
@Setter
public class BotService extends Thread{

    private Message message;

    public BotService(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
       // System.out.println("message.getText() = " + message.getText());

        if (message.hasText()) {

            if (message.getText().equals("/start")){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText("Please sent contact");
                ReplyKeyboardMarkup contact = KeyboardUtil.getContact();
                sendMessage.setReplyMarkup(contact);

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }

        }
        // in progress
    }

    public void workCallbackQuery(CallbackQuery callbackQuery, User user) {

        String data = callbackQuery.getData();

        if (user.getStatus().equals(UserStatus.LANGUAGE)){

            user.setLanguage(data.equals(DemoUtil.LANG_EN) ? Language.EN :
                    data.equals(DemoUtil.LANG_RU) ? Language.RU : Language.UZ);
            DbConnection.updateCustomerLanguage(user.getLanguage().name(),user.getId());

            user.setStatus(UserStatus.HOBBY);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Botdan nima maqsadda foydalanmoqchisiz."
                    : user.getLanguage().equals(Language.EN) ? "What purpose do you want to use the bot for." :
                    "Для каких целей вы хотите использовать бота.");
            sendMessage.setChatId(String.valueOf(user.getId()));

            InlineKeyboardMarkup hobbies = KeyboardUtil.getHobbies(user.getLanguage());
            sendMessage.setReplyMarkup(hobbies);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }else if (user.getStatus().equals(UserStatus.HOBBY)){

            String s = data.replace("hobby_", "");
            user.setHobbyId(Integer.parseInt(s));
            DbConnection.updateCustomerHobby(Integer.parseInt(s),user.getId());

            user.setStatus(UserStatus.GENDER);
            DbConnection.updateCustomerStatus(user.getStatus(), user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Jins: " : user.getLanguage().equals(Language.EN) ?
                    "Gender: " : "Пол: ");
            InlineKeyboardMarkup gender = KeyboardUtil.getGender(user.getLanguage());
            sendMessage.setReplyMarkup(gender);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }else if (user.getStatus().equals(UserStatus.GENDER)){
            user.setGender(data);
            user.setStatus(UserStatus.MENU);
            DbConnection.updateCustomerStatus(user.getStatus(), user.getId());

            user.setRole(Role.CUSTOMER);
            DbConnection.updateCustomerRole(user.getRole(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "<b>ASSALOMU ALEYKUM</b> \nBOTGA XUSH KELIBSIZ.":
                    user.getLanguage().equals(Language.RU) ? "<b> ЗДРАВСТВУЙТЕ  </b>\nДОБРО ПОЖАЛОВАТЬ В БОТА." :
                    "<b> HELLO </b> WELCOME TO BOT.");
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setParseMode(ParseMode.HTML);
            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendMessage.setReplyMarkup(menu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }

    }
}
