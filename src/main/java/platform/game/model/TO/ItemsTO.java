package platform.game.model.TO;

import lombok.Data;

@Data
public class ItemsTO {
    private int i_seq;     // i_seq, int NOT NULL
    private int price; // price, int NOT NULL
    private int period; // period, int NOT NULL

}
// CREATE TABLE `Items` (
// 	`i_seq`	    int	NOT NULL,
// 	`price`	    int	NOT NULL,
// 	`period`	int	NOT NULL
// );