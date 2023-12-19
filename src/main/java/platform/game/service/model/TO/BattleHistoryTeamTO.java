package platform.game.service.model.TO;

import lombok.Data;

@Data
public class BattleHistoryTeamTO {
    private int bth_seq;       // bth_seq, int NOT NULL
    private int t_seq1;         // t_seq1, int NOT NULL
    private int t_seq2;        // t_seq2, int NOT NULL
    private int result;         // result, int NOT NULL
    private int ps_seq;        // ps_seq, int NOT NULL

}
// CREATE TABLE `BattleHistoryTeam` (
// 	`bth_seq`	    int	NOT NULL,
// 	`t_seq`	        int	NOT NULL,
// 	`t_seq2`	    int	NOT NULL,
// 	`result`	    int	NOT NULL,
// 	`ps_seq`	    int	NOT NULL
// );
