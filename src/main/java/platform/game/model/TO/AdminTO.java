package platform.game.model.TO;

public class AdminTO {
    private int seq;        // a_seq, int NOT NULL
    private String auth;    // a_auth, char(6) NOT NULL
    private int uSeq;         // u_seq, int NOT NULL

}
// CREATE TABLE `Admin` (
// 	`a_seq`	    int	    NOT NULL,
// 	`a_auth`	char(6)	NOT NULL,
// 	`u_seq`	    int	    NOT NULL
// );