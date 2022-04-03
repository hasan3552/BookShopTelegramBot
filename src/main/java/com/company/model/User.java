package com.company.model;

import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.UserStatus;
import lombok.*;

@AllArgsConstructor

@Getter
@Setter
@ToString
public class User {

    Long id;
    String username;
    String phoneNumber = null;
    Language language = null;
    Integer hobbyId = null;
    String gender = null;
    Boolean isBlocked;

    UserStatus status;
    Role role;

    public User(Long id, String username) {
        this.id = id;
        this.isBlocked = false;
        this.username = username;
        role = Role.REGISTER;
        status = UserStatus.NEW;
    }
}
