package platform.game.model.TO;

public class ExpHistoryTO {
    private int seq;         // eh_seq, int NOT NULL
    private int uSeq;        // u_seq, int NOT NULL 회원seq
    private int edSeq;       // ed_seq, varchar(100) NOT NULL 경험치 내역 설명seq
    private Integer exp;     // eh_exp, int NOT NULL
    private String date;     // eh_date, datetime NOT NULL

}

// CREATE TABLE `ExpHistory` (
// 	`eh_seq`	int	            NOT NULL,
// 	`u_seq`	    int	            NOT NULL,
// 	`ed_seq`	int         	NOT NULL,
// 	`eh_exp`	int	            NOT NULL,
// 	`eh_date`	datetime	    NOT NULL
// );

// CREATE TABLE `ExpDesc` (
// 	`ed_seq`	int	            NOT NULL,
// 	`ed_desc`	varchar(100)	NOT NULL
// );

