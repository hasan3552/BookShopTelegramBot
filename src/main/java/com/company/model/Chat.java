package com.company.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Chat {

    Integer chatId;
    Integer fromId;
    Integer toId;
    String text;
    Boolean isDeleted = false;

}
