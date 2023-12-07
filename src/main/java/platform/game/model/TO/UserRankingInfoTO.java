package platform.game.model.TO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRankingInfoTO {
    private int uSeq;      // u_seq, int NOT NULL 유저seq
    private Integer rank1;  // ur_rank1, int NOT NULL
    private Integer rank2;  // ur_rank2, int NOT NULL
    private Integer rank3;  // ur_rank3, int NOT NULL
}

// CREATE TABLE `UserRankingInfo` (
// 	`u_seq`	    int	NOT NULL,
// 	`ur_rank1`	int	NOT NULL,
// 	`ur_rank2`	int	NOT NULL,
// 	`ur_rank3`	int	NOT NULL
// );