package platform.game.model.TO;

import lombok.Data;

@Data
public class ConnectionsTO {
    private int c_seq;     // c_seq, int NOT NULL
    private int u_seq;     // u_seq, int NOT NULL
    private String date; // date, datetime NOT NULL
    private String ip;   // ip, varchar(40) NOT NULL

}
// CREATE TABLE `Connections` (
// 	`c_seq`	    int	        NOT NULL,
// 	`u_seq`	    int	        NOT NULL,
// 	`date`	    datetime	NOT NULL,
// 	`ip`	    varchar(40)	NOT NULL
// );