package platform.game.model.TO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleHistoryTeamTO {
    private int seq;       // bth_seq, int NOT NULL
    private int tSeq;         // t_seq, int NOT NULL
    private int tSeq2;        // t_seq2, int NOT NULL
    private Integer result; // bth_result, int NOT NULL
    private int psSeq;        // ps_seq, int NOT NULL

}
// CREATE TABLE `BattleHistoryTeam` (
// 	`bth_seq`	    int	NOT NULL,
// 	`t_seq`	        int	NOT NULL,
// 	`t_seq2`	    int	NOT NULL,
// 	`bth_result`	int	NOT NULL,
// 	`ps_seq`	    int	NOT NULL
// );
