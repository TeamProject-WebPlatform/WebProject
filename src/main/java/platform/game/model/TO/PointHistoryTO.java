package platform.game.model.TO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointHistoryTO {
    private int seq;        // ph_seq, int NOT NULL
    private int uSeq;         // u_seq, int NOT NULL
    private String pdSeq;    // pio_seq, varchar(100) NOT NULL
    private Integer point;   // ph_point, int NOT NULL
    private String date;     // ph_date, datetime NOT NULL

}
// CREATE TABLE `PointHistory` (
// 	`ph_seq`	int	            NOT NULL,
// 	`u_seq`	    int	            NOT NULL,
// 	`pd_seq`	varchar(100)	NOT NULL,
// 	`ph_point`	int	            NOT NULL,
// 	`ph_date`	datetime	    NOT NULL
// );

//포인트 내역 설명 table
// CREATE TABLE `PointDesc` (
// 	`pd_seq`	int	            NOT NULL,
// 	`pd_desc`	varchar(100)	NOT NULL
// );