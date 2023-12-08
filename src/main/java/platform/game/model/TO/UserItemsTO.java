package platform.game.model.TO;

import lombok.Data;

@Data
public class UserItemsTO {
    private int ui_seq; // ui_seq, int NOT NULL
    private int u_seq;  // u_seq, int NOT NULL
    private int i_seq;  // i_seq, int NOT NULL
}
// CREATE TABLE `UserItems` (
// 	`ui_seq`	int	NOT NULL,
// 	`u_seq`	    int	NOT NULL,
// 	`i_seq`	    int	NOT NULL
// );