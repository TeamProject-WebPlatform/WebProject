package platform.game.service.model.TO;

import lombok.Data;

@Data
public class AdminTO {
    private int a_seq;        // a_seq, int NOT NULL
    private String auth;    // a_auth, char(6) NOT NULL
    private int u_seq;         // u_seq, int NOT NULL

}
// CREATE TABLE `Admin` (
// 	`a_seq`	    int	    NOT NULL,
// 	`auth`	    char(6)	NOT NULL,
// 	`u_seq`	    int	    NOT NULL
// );