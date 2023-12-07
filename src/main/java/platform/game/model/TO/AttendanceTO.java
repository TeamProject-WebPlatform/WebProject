package platform.game.model.TO;

public class AttendanceTO {
    private int uSeq;         // u_seq, int NOT NULL
    private String lastDate;  // a_lastDate, datetime NOT NULL
    private Integer totalDate; // a_totalDate, int NOT NULL
    private Integer streakDate; // a_streakDate, int NOT NULL

}
// CREATE TABLE `Attendance` (
// 	`u_seq`	        int	        NOT NULL,
// 	`a_lastDate`	datetime	NOT NULL,
// 	`a_totalDate`	int	        NOT NULL,
// 	`a_streakDate`	int	        NOT NULL
// );
