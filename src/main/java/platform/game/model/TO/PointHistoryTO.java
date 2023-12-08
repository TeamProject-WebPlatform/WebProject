package platform.game.model.TO;

import lombok.Data;

@Data
public class PointHistoryTO {
    private int ph_seq;        // ph_seq, int NOT NULL
    private int u_seq;         // u_seq, int NOT NULL
    private String pd_seq;    // pd_seq, varchar(100) NOT NULL 포인트내역설명seq
    private int point;      // ph_point, int NOT NULL
    private String date;     // ph_date, datetime NOT NULL

}
// CREATE TABLE `PointHistory` (
// 	`ph_seq`	int	            NOT NULL,
// 	`u_seq`	    int	            NOT NULL,
// 	`pd_seq`	varchar(100)	NOT NULL,
// 	`point`	    int	            NOT NULL,
// 	`date`	    datetime	    NOT NULL
// );

//포인트 내역 설명 table
// CREATE TABLE `PointDesc` (
// 	`pd_seq`	int	            NOT NULL,
// 	`desc`	    varchar(100)	NOT NULL
// );