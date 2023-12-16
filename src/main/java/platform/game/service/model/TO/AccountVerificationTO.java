package platform.game.service.model.TO;

import lombok.Data;

@Data
public class AccountVerificationTO {
    private int v_seq; // v_seq, int NOT NULL 
    private int u_seq; // u_seq, int NOT NULL 유저seq
    private int g_seq; // g_seq, int NOT NULL 게임seq

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