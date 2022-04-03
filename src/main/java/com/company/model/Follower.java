package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Follower {

    private Integer id;
    private Long toId;
    private Long withId;
    private LocalDateTime when;
    private String nickname;
    private Boolean isBlocked = false;
    private Boolean isDeleted = false;

    public Follower(Integer id, Long toId, Long withId) {
        this.id = id;
        this.toId = toId;
        this.withId = withId;
        when = LocalDateTime.now();
        nickname = null;
    }
}
