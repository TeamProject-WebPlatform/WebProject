package platform.game.model.TO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserItemsTO {
    private int seq; // ui_seq, int NOT NULL
    private int uSeq;  // u_seq, int NOT NULL
    private int iSeq;  // i_seq, int NOT NULL
}
// CREATE TABLE `UserItems` (
// 	`ui_seq`	int	NOT NULL,
// 	`u_seq`	    int	NOT NULL,
// 	`i_seq`	    int	NOT NULL
// );