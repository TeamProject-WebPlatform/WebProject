package platform.game.model.TO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamTO {
    private int seq;           // t_seq, int NOT NULL
    private String members;    // t_members, varchar(100) NOT NULL
    private String name;       // t_name, varchar(100) NOT NULL
}
// CREATE TABLE `Team` (
// 	`t_seq`	    int	            NOT NULL,
// 	`t_members`	varchar(100)	NOT NULL,
// 	`t_name`	varchar(100)	NOT NULL
// );