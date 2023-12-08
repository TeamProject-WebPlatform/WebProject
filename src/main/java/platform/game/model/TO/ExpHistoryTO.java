package platform.game.model.TO;

import lombok.Data;

@Data
public class ExpHistoryTO {
    private int eh_seq;         // eh_seq, int NOT NULL
    private int u_seq;        // u_seq, int NOT NULL 회원seq
    private int ed_seq;       // ed_seq, varchar(100) NOT NULL 경험치 내역 설명seq
    private int exp;        // exp, int NOT NULL
    private String date;     // date, datetime NOT NULL

}

// CREATE TABLE `ExpHistory` (
// 	`eh_seq`	int	            NOT NULL,
// 	`u_seq`	    int	            NOT NULL,
// 	`ed_seq`	int         	NOT NULL,
// 	`exp`	    int	            NOT NULL,
// 	`date`	    datetime	    NOT NULL
// );

// CREATE TABLE `ExpDesc` (
// 	`ed_seq`	int	            NOT NULL,
// 	`ed_desc`	varchar(100)	NOT NULL
// );

