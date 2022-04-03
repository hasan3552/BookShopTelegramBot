package com.company.controller;

import com.company.Main;
import com.company.db.Database;
import com.company.db.DbConnection;
import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.UserStatus;
import com.company.model.Chat;
import com.company.model.Follower;
import com.company.model.User;
import com.company.service.UserService;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Getter
@Setter
public class UserController extends Thread {

    private Message message;
    private User user;
    private Language language;

    public UserController(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {
        if (user.getStatus().equals(UserStatus.SENDING_REKLAMA)) {

            user.setStatus(UserStatus.MENU);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendMessage.setReplyMarkup(menu);
            sendMessage.setText(language.equals(Language.UZ) ? "Reklamangiz uzatildi. Sizga aloqaga chiqishadi." :
                    language.equals(Language.RU) ? "Ваше объявление отправлено. Они свяжутся с вами." :
                            "Your ad has been forwarded. They will contact you.");
            sendMessage.setChatId(String.valueOf(user.getId()));

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            for (User user1 : Database.customers.stream()
                    .filter(user1 -> user1.getRole().equals(Role.ADMIN)).toList()) {

                if (message.hasPhoto()) {
                    SendPhoto sendPhoto = new SendPhoto();

                    List<PhotoSize> photo = message.getPhoto();
                    String fileId = photo.get(photo.size() - 1).getFileId();

                    InputFile inputFile = new InputFile(fileId);
                    sendPhoto.setPhoto(inputFile);
                    sendPhoto.setChatId(String.valueOf(user1.getId()));
                    sendPhoto.setCaption("<b>REKLAMA !!!</b>\n\n" + message.getCaption());
                    sendPhoto.setParseMode(ParseMode.HTML);

                    if (message.hasReplyMarkup()) {
                        sendMessage.setReplyMarkup(message.getReplyMarkup());
                    }

                    InlineKeyboardMarkup reklama = KeyboardUtil.getReklama();
                    sendPhoto.setReplyMarkup(reklama);

                    Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
                } else {

                    SendMessage sendMessage1 = new SendMessage();
                    sendMessage1.setChatId(String.valueOf(user1.getId()));
                    sendMessage1.setText("<b>REKLAMA</b>\n\n" + message);
                    sendMessage1.setParseMode(ParseMode.HTML);

                    Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);
                }
                String phoneNumber = Database.customers.stream()
                        .filter(user2 -> user2.getId().toString().equals(user.getId().toString()))
                        .findAny().get()
                        .getPhoneNumber();
                SendContact sendContact = new SendContact(String.valueOf(user1.getId()), phoneNumber, "REKLAMA");

                Main.MY_TELEGRAM_BOT.sendMsg(sendContact);
            }
        } else if (user.getStatus().equals(UserStatus.MENU) && (message.getText().equals(DemoUtil.SETTING_UZ) ||
                message.getText().equals(DemoUtil.SETTING_RU) || message.getText().equals(DemoUtil.SETTING_EN))) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Tilni tanlang." : user.getLanguage().equals(Language.RU)
                    ? "Выберите язык." : "Select the language.");
            InlineKeyboardMarkup language = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(language);


            user.setStatus(UserStatus.SETTING);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (user.getStatus().equals(UserStatus.MENU) && (message.getText().equals(DemoUtil.CONTACT_ADMIN_UZ) ||
                message.getText().equals(DemoUtil.CONTACT_ADMIN_RU) || message.getText().equals(DemoUtil.CONTACT_ADMIN_EN))) {

            user.setStatus(UserStatus.CONTACT_ADMIN);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Murojaat maqsadini tanlang:" : language.equals(Language.RU) ?
                    "Выберите цель приложения:" : "Select the purpose of the application:");

            ReplyKeyboardMarkup contactAdmin = KeyboardUtil.getContactAdmin(language);
            sendMessage.setReplyMarkup(contactAdmin);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(UserStatus.CONTACT_ADMIN) && ((message.getText().equals(DemoUtil.PHONE_NUM_EN) ||
                message.getText().equals(DemoUtil.PHONE_NUM_RU) || message.getText().equals(DemoUtil.PHONE_NUM_UZ)))) {

            user.setStatus(UserStatus.MENU);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendContact sendContact = new SendContact(String.valueOf(user.getId()),
                    "+99891 645 35 52", "Hasan");
            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendContact.setReplyMarkup(menu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendContact);

        } else if (user.getStatus().equals(UserStatus.CONTACT_ADMIN) && ((message.getText().equals(DemoUtil.COMPLAINT_UZ) ||
                message.getText().equals(DemoUtil.COMPLAINT_RU) || message.getText().equals(DemoUtil.COMPLAINT_EN)))) {

            user.setStatus(UserStatus.WRITE_COMPLAINT);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Shikoyatingizni yozib qoldiring.\nU albatta adminga " +
                    "yetkaziladi va sizning shikoyatingiz inobatga olinib sizga albatta javob qaytaramiz." : language.equals(Language.RU) ?
                    "Запишите вашу жалобу.\nОна будет доставлена администратору и мы ответим на вашу жалобу." :
                    "Write down your complaint.\nIt will be delivered to the admin and we will respond to your complaint.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(UserStatus.WRITE_COMPLAINT)) {

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
                DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            }
        } else if (user.getStatus().equals(UserStatus.CONTACT_ADMIN) && (message.getText().equals(DemoUtil.REKLAMA_UZ) ||
                message.getText().equals(DemoUtil.REKLAMA_EN) || message.getText().equals(DemoUtil.REKLAMA_RU))) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Relama roligingizni uzating. Biz uniko'rib chiqib sizga" +
                    "bog'lanamiz.\n\n<b>ESLATMA REKLAMANGIZNI UZATGANDA RASM HAM BO'LISHI SHART.</b> "
                    : language.equals(Language.EN) ? "Submit your ad. We will review it and contact you.\n\n" +
                    "<b> NOTE MUST HAVE A PICTURE WHEN YOU SHOW YOUR ADVERTISING. </b>" :
                    "Подать объявление. Мы рассмотрим его и свяжемся с вами.\n\n" +
                            "<b> ПРИМЕЧАНИЕ ДОЛЖНО БЫТЬ ИЗОБРАЖЕНИЕ, КОГДА ВЫ ПОКАЗЫВАЕТЕ ВАШУ РЕКЛАМУ. </b>");
            sendMessage.setParseMode(ParseMode.HTML);

            user.setStatus(UserStatus.SENDING_REKLAMA);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(UserStatus.CONTACT_ADMIN) && (message.getText().equals(DemoUtil.USER_MENU_BACK_UZ) ||
                message.getText().equals(DemoUtil.USER_MENU_BACK_RU) || message.getText().equals(DemoUtil.USER_MENU_BACK_EN))) {

            user.setStatus(UserStatus.MENU);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Asosiy menyu" : language.equals(Language.RU) ?
                    "Главное меню" : "Main menu");

            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
            sendMessage.setReplyMarkup(menu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(UserStatus.MENU) && (message.getText().equals(DemoUtil.CONVERSATION_UZ) ||
                message.getText().equals(DemoUtil.CONVERSATION_RU) || message.getText().equals(DemoUtil.CONVERSATION_EN))) {

            user.setStatus(UserStatus.USER_CONVERSATION);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Suhbatlashish bo'limi" : language.equals(Language.RU) ?
                    "Mеню чата" : "Chat menu");

            InlineKeyboardMarkup converseMenu = KeyboardUtil.getConverseMenu(language);
            sendMessage.setReplyMarkup(converseMenu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(UserStatus.MENU) && (message.getText().equals(DemoUtil.FOLLOWER_UZ) ||
                message.getText().equals(DemoUtil.FOLLOWER_RU) || message.getText().equals(DemoUtil.FOLLOWER_EN))) {

            user.setStatus(UserStatus.USER_FOLLOWER_BUTTON);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Do'stlarim  menyusi" : language.equals(Language.RU) ?
                    "Меню моих друзей" : "Menu my friends");
            InlineKeyboardMarkup frientMenu = KeyboardUtil.getFrientMenu(language);
            sendMessage.setReplyMarkup(frientMenu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(UserStatus.USER_GET_CONTACT)) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));

            if (message.hasContact()) {

                user.setStatus(UserStatus.MENU);
                DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

                String phoneNumber = message.getContact().getPhoneNumber();

                User user1 = null;

                for (User customer : Database.customers) {
                    if (customer.getPhoneNumber() != null && phoneNumber.contains(customer.getPhoneNumber())) {
                        user1 = customer;
                        break;
                    }
                }

                if (user1 != null) {

                    sendMessage.setText(language.equals(Language.UZ) ? "Do'stlashish so'rovingiz foydalanuvchiga uzatildi." +
                            "Agar do'stingiz tasdiqlasa sizga xabar beriladi va siz suhbatlashish imkoniga ega bo'lasiz." :
                            language.equals(Language.RU) ? "Ваш запрос на добавление в друзья был отправлен пользователю. " +
                                    "Если ваш друг подтвердит, вы будете уведомлены, и у вас будет возможность пообщаться." :
                                    "Your friend request has been forwarded to the user. If your friend confirms, you will be " +
                                            "notified and you will have the opportunity to chat.");


                    SendMessage sendMessage1 = new SendMessage();
                    sendMessage1.setChatId(String.valueOf(user1.getId()));
                    sendMessage1.setText(user.getId() + (language.equals(Language.UZ) ?
                            "\nUshbu telegram IDli foydalanuvchi siz bilan do'stlashmoqchi. " : language.equals(Language.RU) ?
                            "Этот пользователь Telegram ID хочет с вами дружить." :
                            "This telegram ID user wants to be friends with you."));

                    InlineKeyboardMarkup friendshipOffer = KeyboardUtil.getFriendshipOffer(user);
                    sendMessage1.setReplyMarkup(friendshipOffer);

                    Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);

                } else {
                    sendMessage.setText(language.equals(Language.UZ) ? "Ushbu raqam egasi bizning botdan ro'yhatdan o'tmagan."
                            : language.equals(Language.RU) ? "Владелец этого номера не зарегистрирован на нашем боте." :
                            "The owner of this number is not registered on our bot.");
                }

            } else {
                sendMessage.setText(language.equals(Language.UZ) ? "Iltimos contact uzating" : language.equals(Language.RU) ?
                        "Пожалуйста, свяжитесь с нами" : "Please make a contact");
            }

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (user.getStatus().equals(UserStatus.USER_WROTE_OTHER)) {

            Optional<Chat> optional = Database.chats.stream()
                    .filter(chat -> String.valueOf(chat.getFromId()).equals(String.valueOf(user.getId()))
                            && chat.getText() == null && !chat.getIsSending())
                    .findAny();

            if (optional.isPresent()) {

                Chat chat = optional.get();
                chat.setIsSending(true);


                user.setStatus(UserStatus.MENU);
                DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

                Optional<Follower> optional1 = Database.followers.stream()
                        .filter(follower -> follower.getToId().equals(chat.getToId())
                                && follower.getWithId().equals(chat.getFromId()))
                        .findAny();

//            if (message.hasContact()) {
//
//            }
//            else
                if (message.hasText()) {
                    chat.setText(message.getText());
                    DbConnection.updateChatTextAndSending(chat.getChatId(), chat.getText());

                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(String.valueOf(chat.getToId()));
                    sendMessage.setText(message.getText() + "\n\n<b>FROM: tg://" + optional1.get().getWithId() + "</b>");
//                    sendMessage.setDisableWebPagePreview(true);
//                    sendMessage.setProtectContent(true);
//                    sendMessage.setAllowSendingWithoutReply(true);

                    InlineKeyboardMarkup chatMarkup = KeyboardUtil.getChatMarkup(chat.getFromId());
                    sendMessage.setReplyMarkup(chatMarkup);
                    sendMessage.setParseMode(ParseMode.HTML);

                    Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

                }
//            else if (message.hasPhoto()) {
//
//            } else if (message.hasAudio()) {
//
//            } else if (message.hasLocation()) {
//
//            }

            }

        } else if (user.getStatus().equals(UserStatus.USER_GET_TELEGRAM_ID)) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));

            try {
                Long parseLong = Long.parseLong(message.getText());

                if (Pattern.matches("[0-9]{10}", message.getText())) {

                    user.setStatus(UserStatus.MENU);
                    DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

                    User user1 = null;

                    for (User customer : Database.customers) {
                        if (customer.getPhoneNumber() != null && parseLong.equals(customer.getId())) {
                            user1 = customer;
                            break;
                        }
                    }

                    if (user1 != null) {

                        sendMessage.setText(language.equals(Language.UZ) ? "Do'stlashish so'rovingiz foydalanuvchiga uzatildi." +
                                "Agar do'stingiz tasdiqlasa sizga xabar beriladi va siz suhbatlashish imkoniga ega bo'lasiz." :
                                language.equals(Language.RU) ? "Ваш запрос на добавление в друзья был отправлен пользователю. " +
                                        "Если ваш друг подтвердит, вы будете уведомлены, и у вас будет возможность пообщаться." :
                                        "Your friend request has been forwarded to the user. If your friend confirms, you will be " +
                                                "notified and you will have the opportunity to chat.");


                        SendMessage sendMessage1 = new SendMessage();
                        sendMessage1.setChatId(String.valueOf(user1.getId()));
                        sendMessage1.setText(user.getId() + (language.equals(Language.UZ) ?
                                "\nUshbu telegram IDli foydalanuvchi siz bilan do'stlashmoqchi. " : language.equals(Language.RU) ?
                                "Этот пользователь Telegram ID хочет с вами дружить." :
                                "This telegram ID user wants to be friends with you."));

                        InlineKeyboardMarkup friendshipOffer = KeyboardUtil.getFriendshipOffer(user);
                        sendMessage1.setReplyMarkup(friendshipOffer);

                        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);

                    } else {
                        sendMessage.setText(language.equals(Language.UZ) ?
                                "Ushbu telegram IDli bizning botdan ro'yhatdan o'tmagan." : language.equals(Language.RU) ?
                                "Этот идентификатор телеграммы не зарегистрирован в нашем боте." :
                                "This telegram ID is not registered on our bot.");
                    }

                } else {
                    sendMessage.setText(language.equals(Language.UZ) ? "Iltimos faqat telegram ID kiriting" :
                            language.equals(Language.RU) ? "Пожалуйста, введите только идентификатор телеграммы" :
                                    "Please enter only the telegram ID");
                }

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);


            } catch (Exception e) {

                sendMessage.setText(language.equals(Language.UZ) ? "Iltimos faqat telegram ID kiriting" :
                        language.equals(Language.RU) ? "Пожалуйста, введите только идентификатор телеграммы" :
                                "Please enter only the telegram ID");


                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }


        } else if (user.getStatus().equals(UserStatus.USER_GIVE_NICKNAME)) {

            if (message.hasText()) {
                user.setStatus(UserStatus.MENU);
                DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            }
            Optional<Follower> optional = Database.followers.stream()
                    .filter(follower -> follower.getToId().equals(user.getId()) && follower.getNickname() == null)
                    .findAny();

            if (optional.isPresent()) {

                Follower follower = optional.get();
                follower.setNickname(message.getText());
                DbConnection.updateFollowerNickname(follower.getId(),follower.getNickname());

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(language.equals(Language.UZ) ? "<b>Suhbatlashish</b>" :
                        language.equals(Language.RU) ? "<b> Чат </b>" : "<b> Chat </b>");
                sendMessage.setParseMode(ParseMode.HTML);

                InlineKeyboardMarkup chatMarkup = KeyboardUtil.getChatMarkup(follower.getWithId());
                sendMessage.setReplyMarkup(chatMarkup);

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

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
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            user.setPhoneNumber(message.getContact().getPhoneNumber());
            DbConnection.updateCustomerPhoneNumber(message.getContact().getPhoneNumber(), user.getId());

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }

    public void workCallbackQuery(CallbackQuery callbackQuery, User user) {

        String data = callbackQuery.getData();

        if (user.getStatus().equals(UserStatus.SETTING) && (data.equals(DemoUtil.LANG_UZ) ||
                data.equals(DemoUtil.LANG_RU) || data.equals(DemoUtil.LANG_EN))) {

            user.setLanguage(data.equals(DemoUtil.LANG_UZ) ? Language.UZ : data.equals(DemoUtil.LANG_RU) ?
                    Language.RU : Language.EN);
            DbConnection.updateCustomerLanguage(user.getLanguage().name(),user.getId());

            user.setStatus(UserStatus.MENU);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Til o'zgartirildi." :
                    user.getLanguage().equals(Language.RU) ? "Язык изменен." : "Language changed.");

            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendMessage.setReplyMarkup(menu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if ((user.getStatus().equals(UserStatus.USER_CONVERSATION) || user.getStatus().equals(UserStatus.USER_FOLLOWER_BUTTON)) &&
                data.equals(DemoUtil.BACK_FROM_CONVERSE)) {

            user.setStatus(UserStatus.MENU);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

        } else if (user.getStatus().equals(UserStatus.USER_FOLLOWER_BUTTON) && data.equals(DemoUtil.SHOW_MY_FOLLOWERS)) {

            UserService userService = new UserService(message, user);
            userService.showMyFriends();

        } else if ((user.getStatus().equals(UserStatus.USER_FOLLOWER_BUTTON) && data.equals(DemoUtil.ADD_FRIEND_USER))
                || (user.getStatus().equals(UserStatus.USER_CONVERSATION) && data.equals(DemoUtil.WITH_NEW_FRIEND))) {

            UserService service = new UserService(message, user);
            service.addFriend();


        } else if (user.getStatus().equals(UserStatus.USER_FOLLOWER_BUTTON) && data.equals(DemoUtil.DELETE_FRIEND_USER)) {
            // in progress


        } else if (data.equals(DemoUtil.BY_PHONE_NUMBER)) {

            user.setStatus(UserStatus.USER_GET_CONTACT);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Do'stlashmoqchi bo'lgan foydalanuvchining telefon raqamini kiriting."
                    : language.equals(Language.RU) ? "Введите номер телефона пользователя, с которым хотите дружить." :
                    "Enter the phone number of the user you want to be friends with.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.equals(DemoUtil.BY_USER_ID)) {

            user.setStatus(UserStatus.USER_GET_TELEGRAM_ID);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Do'stlashmoqchi bo'lgan foydalanuvchining telegram IDsini kiriting." : language.equals(Language.RU)
                    ? "Введите идентификатор телеграммы пользователя, с которым хотите дружить." :
                    "Enter the telegram ID of the user you want to be friends with.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (Pattern.matches("truee[0-9]{10}", data)) {
            String truee = data.replace("truee", "");

            User user2 = Database.customers.stream()
                    .filter(user1 -> String.valueOf(user1.getId()).equals(truee))
                    .findAny().get();

            Language language = user2.getLanguage();

            user2.setStatus(UserStatus.USER_GIVE_NICKNAME);
            DbConnection.updateCustomerStatus(user2.getStatus(),user2.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user2.getId()));
            sendMessage.setText("<b>" + user2.getId() + "</b>" +
                    (language.equals(Language.UZ) ? "\n\nUshbu telegram ID foydalanuvchi so'rovingizni qabul qildi." +
                            "\nDo'stingizga taxallus bering."
                            : language.equals(Language.RU) ? "\n\nЭтот идентификатор телеграммы принял" +
                            " ваш запрос пользователя.\nДайте своему другу прозвище." :
                            "\n\nThis telegram ID has received your user request.\nGive your friend a nickname."));
            sendMessage.setParseMode(ParseMode.HTML);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            user.setStatus(UserStatus.USER_GIVE_NICKNAME);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            SendMessage sendMessage1 = new SendMessage();

            Language language1 = user.getLanguage();
            sendMessage1.setChatId(String.valueOf(user.getId()));
            sendMessage1.setText("<b>" + user2.getId() + "</b>" + (language1.equals(Language.UZ) ?
                    "\n\nDo'stingizga taxallus bering." : language1.equals(Language.RU) ?
                    "\n\nДайте своему другу прозвище." : "\n\nGive your friend a nickname."));
            sendMessage1.setParseMode(ParseMode.HTML);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);

            Follower follower = new Follower(Database.followers.size(), user.getId(), user2.getId());
            Database.followers.add(follower);
            DbConnection.addFollower(follower);

            Follower follower1 = new Follower(Database.followers.size() + 1, user2.getId(), user.getId());
            Database.followers.add(follower1);
            DbConnection.addFollower(follower1);

        } else if (Pattern.matches("[0-9]{10}", data)) {

            user.setStatus(UserStatus.USER_WROTE_OTHER);
            DbConnection.updateCustomerStatus(user.getStatus(),user.getId());

            Chat chat = new Chat(user.getId(), Long.parseLong(data), null);
            Database.chats.add(chat);
            DbConnection.addChat(chat);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "<b>XABARINGIZNI YOZING</b>" : language.equals(Language.RU) ?
                    "НАПИШИ СООБЩЕНИЕ" : "WRITE MESSAGE");
            sendMessage.setParseMode(ParseMode.HTML);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }
}
