package platform.game.service.model.TO;

import lombok.Data;

@Data
public class TeamTO {
    private int t_seq;          // t_seq, int NOT NULL
    private String members;     // members, varchar(100) NOT NULL
    private String name;        // name, varchar(100) NOT NULL
}
// CREATE TABLE `Team` (
// 	`t_seq`	    int	            NOT NULL,
// 	`members`	varchar(100)	NOT NULL,
// 	`name`	    varchar(100)	NOT NULL
// );