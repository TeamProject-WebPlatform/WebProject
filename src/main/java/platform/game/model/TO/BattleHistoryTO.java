package platform.game.model.TO;

import lombok.Data;

@Data
public class BattleHistoryTO {
    private int bth_seq;       // bth_seq, int NOT NULL
    private int u_seq1;        // u_seq1, int NOT NULL
    private int u_seq2;        // u_seq2, int NOT NULL
    private String result;      // result, varchar(10) NOT NULL
    private int ps_seq;        // ps_seq, int NOT NULL
}
// CREATE TABLE `BattleHistory` (
// 	`bth_seq`	    int	        NOT NULL,
// 	`u_seq1`	    int	        NOT NULL,
// 	`u_seq2`	    int	        NOT NULL,
// 	`result`	varchar(10) NOT NULL,
// 	`ps_seq`	    int	        NOT NULL
// );

// 포인트 명세 테이블
// CREATE TABLE `PointSpecification` (
// 	`ps_seq`	int	            NOT NULL,
// 	`desc`	varchar(1000)	NOT NULL
// );
