package com.ssafy.board.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Article {
    private Long articleNo;
    private String userId;
    private String subject;
    private String content;
    private Long hit;
    private LocalDateTime registerTime;
}