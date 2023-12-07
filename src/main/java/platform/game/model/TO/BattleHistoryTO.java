package platform.game.model.TO;

public class BattleHistoryTO {
    private int seq;       // bth_seq, int NOT NULL
    private int uSeq1;        // u_seq1, int NOT NULL
    private int uSeq2;        // u_seq2, int NOT NULL
    private String result;      // bth_result, varchar(10) NOT NULL
    private int psSeq;        // ps_seq, int NOT NULL
}
// CREATE TABLE `BattleHistory` (
// 	`bth_seq`	    int	        NOT NULL,
// 	`u_seq1`	    int	        NOT NULL,
// 	`u_seq2`	    int	        NOT NULL,
// 	`bth_result`	varchar(10) NOT NULL,
// 	`ps_seq`	    int	        NOT NULL
// );

// 포인트 명세 테이블
// CREATE TABLE `PointSpecification` (
// 	`ps_seq`	int	            NOT NULL,
// 	`ps_desc`	varchar(1000)	NOT NULL
// );
