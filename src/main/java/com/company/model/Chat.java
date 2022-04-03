package com.company.model;

import com.company.db.Database;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Chat {

    Long chatId;
    Long fromId;
    Long toId;
    String text;
    Boolean isDeleted = false;
    Boolean isSending =  false;

    public Chat(Long fromId, Long toId, String text) {
        chatId = (long) Database.chats.size();
        this.fromId = fromId;
        this.toId = toId;
        this.text = text;
    }
}
