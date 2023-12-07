package platform.game.model.TO;

public class ReportTO {
    private int seq;         // r_seq, int NOT NULL
    private int uSeq1;        // u_seq1, int NOT NULL
    private int uSeq2;        // u_seq2, int NOT NULL
    private String date;     // r_date, datetime NOT NULL
    private String desc;     // r_desc, varchar(1000) NOT NULL
    private int pSeq;         // p_seq, int NOT NULL
}

// CREATE TABLE `Report` (
// 	`r_seq`	    int	            NOT NULL,
// 	`u_seq1`	int	            NOT NULL,
// 	`u_seq2`	int	            NOT NULL,
// 	`r_date`	datetime	    NOT NULL,
// 	`r_desc`	varchar(1000)	NOT NULL,
// 	`p_seq`	    int	            NOT NULL
// );
