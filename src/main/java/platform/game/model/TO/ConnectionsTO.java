package platform.game.model.TO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionsTO {
    private int seq;     // c_seq, int NOT NULL
    private int uSeq;     // u_seq, int NOT NULL
    private String date; // c_date, datetime NOT NULL
    private String ip;   // c_ip, varchar(40) NOT NULL

}
// CREATE TABLE `Connections` (
// 	`c_seq`	    int	        NOT NULL,
// 	`u_seq`	    int	        NOT NULL,
// 	`c_date`	datetime	NOT NULL,
// 	`c_ip`	    varchar(40)	NOT NULL
// );