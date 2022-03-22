package com.company.model;

import com.company.enums.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    Long id;
    String firstName;
    String lastName;
    Boolean isBlocked;
    String phoneNumber;
    List<Role> roles;
    String username;
    List<Integer> friendsId = new ArrayList<>();
    List<Integer> chatId = new ArrayList<>();


}
