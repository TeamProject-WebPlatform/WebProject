package platform.game.model.TO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRankingInfoTO {
    private int u_seq;      // u_seq, int NOT NULL 유저seq
    private Integer rank1;  // rank1, int NOT NULL
    private Integer rank2;  // rank2, int NOT NULL
    private Integer rank3;  // rank3, int NOT NULL
}

// CREATE TABLE `UserRankingInfo` (
// 	`u_seq`	    int	NOT NULL,
// 	`rank1`	    int	NOT NULL,
// 	`rank2`	    int	NOT NULL,
// 	`rank3`	    int	NOT NULL
// );