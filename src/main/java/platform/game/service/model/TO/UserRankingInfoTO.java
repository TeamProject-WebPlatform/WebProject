package platform.game.service.model.TO;

import lombok.Data;

@Data
public class UserRankingInfoTO {
    private int u_seq;      // u_seq, int NOT NULL 유저seq
    private int rank1;      // rank1, int NOT NULL
    private int rank2;      // rank2, int NOT NULL
    private int rank3;      // rank3, int NOT NULL
}

// CREATE TABLE `UserRankingInfo` (
// 	`u_seq`	    int	NOT NULL,
// 	`rank1`	    int	NOT NULL,
// 	`rank2`	    int	NOT NULL,
// 	`rank3`	    int	NOT NULL
// );