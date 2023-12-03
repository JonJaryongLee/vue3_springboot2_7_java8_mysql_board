package com.ssafy.board.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Member {
    private String userId;
    private String userName;
    private String userPwd;
    private String emailId;
    private String emailDomain;
    private LocalDateTime joinDate;
}