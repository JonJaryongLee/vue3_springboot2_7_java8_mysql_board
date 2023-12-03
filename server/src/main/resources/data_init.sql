CREATE DATABASE ssafy CHARACTER SET utf8 COLLATE utf8_general_ci;

use ssafy;

CREATE TABLE Member (
    userId VARCHAR(255) NOT NULL,
    userName VARCHAR(255) NOT NULL,
    userPwd VARCHAR(255) NOT NULL,
    emailId VARCHAR(255),
    emailDomain VARCHAR(255),
    joinDate DATETIME NOT NULL,
    PRIMARY KEY (userId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Article (
    articleNo BIGINT NOT NULL AUTO_INCREMENT,
    userId VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    hit BIGINT NOT NULL,
    registerTime DATETIME NOT NULL,
    PRIMARY KEY (articleNo),
    FOREIGN KEY (userId) REFERENCES Member(userId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
