package platform.game.service.model.TO;

import lombok.Data;

@Data
public class AttendanceTO {
    private int u_seq;         // u_seq, int NOT NULL
    private String lastDate;  // lastDate, datetime NOT NULL
    private int totalDate;   // totalDate, int NOT NULL
    private int streakDate;  // streakDate, int NOT NULL

}
// CREATE TABLE `Attendance` (
// 	`u_seq`	        int	        NOT NULL,
// 	`lastDate`	    datetime	NOT NULL,
// 	`totalDate`	    int	        NOT NULL,
// 	`streakDate`	int	        NOT NULL
// );
