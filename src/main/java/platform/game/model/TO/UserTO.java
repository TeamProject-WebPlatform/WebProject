package platform.game.model.TO;

import lombok.Data;
@Data
public class UserTO {
    private int u_seq;          // u_seq, int NOT NULL
    private String id;        // id, varchar(20) NOT NULL
    private String password;  // password, varchar(12) NOT NULL
    private String nickname;  // nickname, varchar(10) NOT NULL
    private String u_mail;      // u_mail, varchar(50) NOT NULL
    private String u_date;      // u_date, datetime NOT NULL
    private int u_point;    // u_point, int NOT NULL
    private int u_exp;      // u_exp, int NOT NULL
    private int u_level;    // u_level, int NOT NULL
    private int u_oauth;        // u_oauth, int NOT NULL
    // 0 : 우리 웹, 1 : 카카오, 2 : 스팀 ...
}

// CREATE TABLE `User` (
// 	`u_seq`	    int	        NOT NULL,
// 	`id`	    varchar(20)	NOT NULL,
// 	`password`	varchar(12)	NOT NULL,
// 	`nickname`	varchar(10)	NOT NULL,
// 	`u_mail`	varchar(50)	NOT NULL,
// 	`u_date`	datetime	NOT NULL,
// 	`u_point`	int	        NOT NULL,
// 	`u_exp`	    int	        NOT NULL,
// 	`u_level`	int	        NOT NULL,
// 	`u_oauth`	int     	NOT NULL
// );
