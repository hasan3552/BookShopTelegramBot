package com.company.util;

import com.company.db.Database;
import com.company.enums.Language;
import com.company.model.Hobby;
import com.company.model.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtil {


    public static ReplyKeyboardMarkup getContact() {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setRequestContact(true);
        button.setText("\uD83D\uDCDE Contact");

        row.add(button);
        rowList.add(row);


        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup getLanguage() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("\uD83C\uDDFA\uD83C\uDDFF UZBEK");
        button.setCallbackData(DemoUtil.LANG_UZ);
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("\uD83C\uDDF7\uD83C\uDDFA RUSSIAN");
        button1.setCallbackData(DemoUtil.LANG_RU);
        row1.add(button1);
        rowList.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F ENGLISH");
        button2.setCallbackData(DemoUtil.LANG_EN);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static InlineKeyboardMarkup getHobbies(Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Hobby hobby : Database.hobbies) {
            if (!hobby.getIsDelete()) {

                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(language.equals(Language.UZ) ? hobby.getNameUz() : language.equals(Language.EN)
                        ? hobby.getNameEn() : hobby.getNameRu());
                button.setCallbackData("hobby_" + hobby.getId());
                row.add(button);
                rowList.add(row);
            }

        }

        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getGender(Language language) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(language.equals(Language.UZ) ? "\uD83D\uDE4B\uD83C\uDFFB\u200D♂️ Erkak" :
                language.equals(Language.RU) ? "\uD83D\uDE4B\uD83C\uDFFB\u200D♂️ Мужчина" :
                        "\uD83D\uDE4B\uD83C\uDFFB\u200D♂️ Male");
        button.setCallbackData("male");
        row.add(button);
        rowList.add(row);


        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ? "\uD83D\uDE4B\u200D♀️ Ayol" :
                language.equals(Language.RU) ? "\uD83D\uDE4B\u200D♀️ Женский" :
                        "\uD83D\uDE4B\u200D♀️ Female");
        button1.setCallbackData("female");
        row1.add(button1);
        rowList.add(row1);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static ReplyKeyboardMarkup getMenu(Language language) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(language.equals(Language.UZ) ? DemoUtil.CONVERSATION_UZ : language.equals(Language.RU) ?
                DemoUtil.CONVERSATION_RU : DemoUtil.CONVERSATION_EN);
        row.add(button);
        rowList.add(row);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        button1.setText(language.equals(Language.UZ) ? DemoUtil.FOLLOWER_UZ : language.equals(Language.RU) ?
                DemoUtil.FOLLOWER_RU : DemoUtil.FOLLOWER_EN);
        row1.add(button1);
        rowList.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button2 = new KeyboardButton();
        button2.setText(language.equals(Language.UZ) ? DemoUtil.SETTING_UZ : language.equals(Language.RU) ?
                DemoUtil.SETTING_RU : DemoUtil.SETTING_EN);
        row2.add(button2);

        KeyboardButton button3 = new KeyboardButton();
        button3.setText(language.equals(Language.UZ) ? DemoUtil.CONTACT_ADMIN_UZ : language.equals(Language.RU) ?
                DemoUtil.CONTACT_ADMIN_RU : DemoUtil.CONTACT_ADMIN_EN);
        row2.add(button3);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static ReplyKeyboardMarkup getContactAdmin(Language language) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(language.equals(Language.UZ) ? DemoUtil.PHONE_NUM_UZ :
                language.equals(Language.RU) ? DemoUtil.PHONE_NUM_RU : DemoUtil.PHONE_NUM_EN);
        row.add(button);
        rowList.add(row);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton(language.equals(Language.UZ) ? DemoUtil.COMPLAINT_UZ :
                language.equals(Language.RU) ? DemoUtil.COMPLAINT_RU : DemoUtil.COMPLAINT_EN);
        row1.add(button1);
        rowList.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button2 = new KeyboardButton(language.equals(Language.UZ) ? DemoUtil.REKLAMA_UZ :
                language.equals(Language.RU) ? DemoUtil.REKLAMA_RU : DemoUtil.REKLAMA_EN);
        row2.add(button2);
        rowList.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        KeyboardButton button3 = new KeyboardButton(language.equals(Language.UZ) ? DemoUtil.USER_MENU_BACK_UZ :
                language.equals(Language.RU) ? DemoUtil.USER_MENU_BACK_RU : DemoUtil.USER_MENU_BACK_EN);
        row3.add(button3);
        rowList.add(row3);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup getForAdminComplaint(User user1, User user) {
        Language language = user1.getLanguage();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(language.equals(Language.UZ) ? "JAVOB QAYTARISH" : language.equals(Language.RU) ?
                "ОТВЕТ ВОЗВРАТ" : "ANSWER RETURN");
        button.setCallbackData(user.getId() + "/" + user1.getId());
        row.add(button);
        rowList.add(row);
        markup.setKeyboard(rowList);

        return markup;
    }

    public static ReplyKeyboardMarkup getAdminMenu(Language language) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(language.equals(Language.UZ) ? DemoUtil.SEND_REKLAMA_UZ :
                language.equals(Language.RU) ? DemoUtil.SEND_REKLAMA_RU : DemoUtil.SEND_REKLAMA_EN);
        row.add(button);
        rowList.add(row);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton(language.equals(Language.UZ) ? DemoUtil.ALL_USERS_UZ :
                language.equals(Language.RU) ? DemoUtil.ALL_USERS_RU : DemoUtil.ALL_USERS_EN);
        row1.add(button1);
        rowList.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button2 = new KeyboardButton(language.equals(Language.UZ) ? DemoUtil.SETTING_UZ :
                language.equals(Language.RU) ? DemoUtil.SETTING_RU : DemoUtil.SETTING_EN);
        row2.add(button2);
        rowList.add(row2);


        KeyboardRow row3 = new KeyboardRow();
        KeyboardButton button3 = new KeyboardButton(language.equals(Language.UZ) ? DemoUtil.ADD_ADMIN_BUTTON_UZ :
                language.equals(Language.RU) ? DemoUtil.ADD_ADMIN_BUTTON_RU : DemoUtil.ADD_ADMIN_BUTTON_EN);
        row3.add(button3);
        rowList.add(row3);

        markup.setKeyboard(rowList);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public static InlineKeyboardMarkup getReklama() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("SENDING");
        button.setCallbackData(DemoUtil.REKLAMA_SENDING_INLINE);
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("DELETED");
        button1.setCallbackData(DemoUtil.REKLAMA_DELETE_INLINE);
        row1.add(button1);
        rowList.add(row1);

        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getConverseMenu(Language language) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(language.equals(Language.UZ) ? " DO'STIM BILAN" : language.equals(Language.RU) ?
                "С МОИМ ДРУГОМ" : " WITH MY FRIEND");
        button.setCallbackData(DemoUtil.WITH_MY_FRIEND);
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ? "YANGI DO'ST BILAN" : language.equals(Language.RU) ?
                " С НОВЫМ ДРУГОМ" : "WITH NEW FRIEND");
        button1.setCallbackData(DemoUtil.WITH_NEW_FRIEND);
        row1.add(button1);
        rowList.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "TASODIFIY DO'ST" : language.equals(Language.RU) ?
                "СЛУЧАЙНЫЙ ДРУГ" : "RANDOM FRIEND");
        button2.setCallbackData(DemoUtil.WITH_RANDOM_FRIEND);
        row2.add(button2);
        rowList.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(language.equals(Language.UZ) ? "ORQAGA" : language.equals(Language.RU) ?
                "НАЗАД" : "BACK");
        button3.setCallbackData(DemoUtil.BACK_FROM_CONVERSE);
        row3.add(button3);
        rowList.add(row3);

        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getFrientMenu(Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(language.equals(Language.UZ) ? "MENING DO'STLARIM" : language.equals(Language.RU) ?
                "ДРУЗЬЯ МОИ" : "MY FOLLOWER");
        button.setCallbackData(DemoUtil.SHOW_MY_FOLLOWERS);
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ? "DO'STLASHISH" : language.equals(Language.RU) ?
                "ПОДРУЖИТЕСЬ" : "FRIENDSHIP");
        button1.setCallbackData(DemoUtil.ADD_FRIEND_USER);
        row1.add(button1);
        rowList.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "DO'STLIKNI O'CHIRISH" : language.equals(Language.RU) ?
                "УДАЛИТЬ ДРУЖБУ" : "DELETE FRIENDSHIP");
        button2.setCallbackData(DemoUtil.DELETE_FRIEND_USER);
        row2.add(button2);
        rowList.add(row2);


        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(language.equals(Language.UZ) ? "ORQAGA" : language.equals(Language.RU) ?
                "НАЗАД" : "BACK");
        button3.setCallbackData(DemoUtil.BACK_FROM_CONVERSE);
        row3.add(button3);
        rowList.add(row3);

        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getFriendShipMenu(Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(language.equals(Language.UZ) ? "Telefon raqam orqali" : language.equals(Language.RU) ?
                "По номеру телефона" : "By phone number");
        button.setCallbackData(DemoUtil.BY_PHONE_NUMBER);
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ? "Telegram id orqali" : language.equals(Language.RU) ?
                "Через телеграм id" : "By telegram id");
        button1.setCallbackData(DemoUtil.BY_USER_ID);
        row1.add(button1);
        rowList.add(row1);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup getFriendshipOffer(User user) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("✅");
        button.setCallbackData(DemoUtil.TRUE + user.getId());
        row.add(button);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("❌");
        button1.setCallbackData(DemoUtil.FALSE);
        row.add(button1);

        rowList.add(row);
        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getChatMarkup(User toUser) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("✏️ RESPONSE");
        button.setCallbackData(String.valueOf(toUser.getId()));
        row.add(button);
        rowList.add(row);

        markup.setKeyboard(rowList);
        return markup;

    }
}
