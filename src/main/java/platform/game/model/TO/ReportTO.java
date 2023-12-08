package platform.game.model.TO;

import lombok.Data;

@Data
public class ReportTO {
    private int r_seq;         // r_seq, int NOT NULL
    private int u_seq1;        // u_seq1, int NOT NULL
    private int u_seq2;        // u_seq2, int NOT NULL
    private String date;     // date, datetime NOT NULL
    private String desc;     // desc, varchar(1000) NOT NULL
    private int p_seq;         // p_seq, int NOT NULL
}

// CREATE TABLE `Report` (
// 	`r_seq`	    int	            NOT NULL,
// 	`u_seq1`	int	            NOT NULL,
// 	`u_seq2`	int	            NOT NULL,
// 	`date`	    datetime	    NOT NULL,
// 	`desc`	    varchar(1000)	NOT NULL,
// 	`p_seq`	    int	            NOT NULL
// );
