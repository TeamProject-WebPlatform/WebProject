package platform.game.model.TO;

import lombok.Data;

@Data
public class PunishmentTO {
    private int p_seq;         // p_seq, int NOT NULL
    private int u_seq;         // u_seq, int NOT NULL
    private String period;   // period, varchar(50) NOT NULL
    private String desc;     // desc, varchar(1000) NOT NULL

}
// CREATE TABLE `Punishment` (
// 	`p_seq`	    int	            NOT NULL,
// 	`u_seq`	    int	            NOT NULL,
// 	`period`	varchar(50)	    NOT NULL,
// 	`desc`	varchar(1000)	NOT NULL
// );