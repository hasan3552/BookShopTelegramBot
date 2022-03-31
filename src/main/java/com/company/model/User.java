package com.company.model;

import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.UserStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor

@Getter
@Setter
@ToString
public class User {

    Long id;
    String username;
    String phoneNumber;
    Language language;
    Integer hobbyId;
    String gender;
    Boolean isBlocked;

    UserStatus status;
    Role role;
//    List<Long> friendsId = new ArrayList<>();
//    List<Integer> chatId = new ArrayList<>();

    public User(Long id, String username) {
        this.id = id;
        this.isBlocked = false;
        this.username = username;
        role = Role.REGISTER;
        status = UserStatus.NEW;
    }
}
