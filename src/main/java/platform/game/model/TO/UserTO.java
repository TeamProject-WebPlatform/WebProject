package platform.game.model.TO;

public class UserTO {
    private int seq;          // u_seq, int NOT NULL
    private String name;      // u_name, varchar(10) NOT NULL
    private String id;        // id, varchar(20) NOT NULL
    private String password;  // password, varchar(12) NOT NULL
    private String birth;     // u_birth, datetime NOT NULL
    private String mail;      // u_mail, varchar(50) NOT NULL
    private String date;      // u_date, datetime NOT NULL
    private Integer point;    // u_point, int NOT NULL
    private Integer exp;      // u_exp, int NOT NULL
    private Integer level;    // u_level, int NOT NULL
    private String nickname;  // nickname, varchar(10) NOT NULL
}

// CREATE TABLE `User` (
// 	`u_seq`	    int	        NOT NULL,
// 	`u_name`	varchar(10)	NOT NULL,
// 	`id`	    varchar(20)	NOT NULL,
// 	`password`	varchar(12)	NOT NULL,
// 	`u_birth`	datetime	NOT NULL,
// 	`u_mail`	varchar(50)	NOT NULL,
// 	`u_date`	datetime	NOT NULL,
// 	`u_point`	int	        NOT NULL,
// 	`u_exp`	    int	        NOT NULL,
// 	`u_level`	int	        NOT NULL,
// 	`nickname`	varchar(10)	NOT NULL
// );
