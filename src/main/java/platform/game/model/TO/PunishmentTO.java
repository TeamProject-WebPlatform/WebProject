package platform.game.model.TO;

public class PunishmentTO {
    private int seq;         // p_seq, int NOT NULL
    private int uSeq;         // u_seq, int NOT NULL
    private String period;   // p_period, varchar(50) NOT NULL
    private String desc;     // p_desc, varchar(1000) NOT NULL

}
// CREATE TABLE `Punishment` (
// 	`p_seq`	    int	            NOT NULL,
// 	`u_seq`	    int	            NOT NULL,
// 	`p_period`	varchar(50)	    NOT NULL,
// 	`p_desc`	varchar(1000)	NOT NULL
// );