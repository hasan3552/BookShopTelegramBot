package com.company.util;

import com.company.db.Database;
import com.company.enums.Language;
import com.company.model.Hobby;
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
}
