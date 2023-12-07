package platform.game.model.TO;

public class AccountVerificationTO {
    private int seq; // v_seq, int NOT NULL 
    private int uSeq; // u_seq, int NOT NULL 유저seq
    private int gSeq; // g_seq, int NOT NULL 게임seq

}

// CREATE TABLE `Account Verification` (
// 	`v_seq`	int	NOT NULL,
// 	`u_seq`	int	NOT NULL,
// 	`g_seq`	int	NOT NULL
// );

// 게임 인증 리스트. 우리가 관리할 게임 목록
// CREATE TABLE `VerificationGameList` (
// 	`g_seq`	    int	        NOT NULL,
// 	`g_name`	varchar(50)	NOT NULL
// );