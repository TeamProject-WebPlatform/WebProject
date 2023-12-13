package platform.game.model.TO;

import lombok.Data;
@Data
public class UserTO {
    private int u_seq;          // u_seq, int NOT NULL
    private String id;        // id, varchar(20) NOT NULL
    private String password;  // password, varchar(12) NOT NULL
    private String nickname;  // nickname, varchar(10) NOT NULL
    private String mail;      // mail, varchar(50) NOT NULL
    private String date;      // date, datetime NOT NULL
    private int point;    // point, int NOT NULL
    private int exp;      // exp, int NOT NULL
    private int level;    // level, int NOT NULL
}

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
