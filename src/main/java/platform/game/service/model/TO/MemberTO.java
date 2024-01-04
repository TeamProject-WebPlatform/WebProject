package platform.game.service.model.TO;

import lombok.Data;
@Data
public class MemberTO{
    private int member_id;          // member_id, int NOT NULL
    private String userid;        // id, varchar(20) NOT NULL
    private String password;  // password, varchar(70) NOT NULL
    private String email;      // email, varchar(50) NOT NULL
    private String date;      // date, datetime NOT NULL
    private int point;    // point, int NOT NULL
    private int exp;      // exp, int NOT NULL
    private int level;    // level, int NOT NULL
    private String nickname;  // nickname, varchar(10) NOT NULL
    private int mail_cert;  //mail_cert, int NOT NULL
    private int authority;  //authority, int NOT NULL

}

// CREATE TABLE member (
//     member_id INT NOT NULL PRIMARY KEY,
//     userid VARCHAR(20) NOT NULL DEFAULT '',
//     password VARCHAR(70) NOT NULL DEFAULT '',
//     email VARCHAR(50) NOT NULL DEFAULT '',
//     date DATETIME DEFAULT CURRENT_TIMESTAMP,
//     point INT NOT NULL DEFAULT 0,
//     exp INT NOT NULL DEFAULT 0,
//     level INT NOT NULL DEFAULT 1,
//     nickname VARCHAR(10) NOT NULL DEFAULT '',
//     mail_cert INT NOT NULL DEFAULT 0,
//     authority INT NOT NULL DEFAULT 0
// );


//옛날꺼
// CREATE TABLE `User` (
// 	`u_seq`	    int	        NOT NULL,
// 	`id`	    varchar(20)	NOT NULL,
// 	`password`	varchar(12)	NOT NULL,
// 	`nickname`	varchar(10)	NOT NULL,
// 	`mail`	    varchar(50)	NOT NULL,
// 	`date`	    datetime	NOT NULL,
// 	`point`	    int	        NOT NULL,
// 	`exp`	    int	        NOT NULL,
// 	`level`	    int	        NOT NULL,
// 	`oauth`	    int     	NOT NULL
// );