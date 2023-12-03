package com.ssafy.board.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginDto {

    private String userId;
    private String userPwd;

    public LoginDto(String userId, String userPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
    }
}
