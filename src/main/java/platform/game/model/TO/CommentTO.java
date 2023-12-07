package platform.game.model.TO;

public class CommentTO {
    private int seq;           // c_seq, int NOT NULL
    private int pSeq;           // p_seq, int NOT NULL
    private int uSeq;           // u_seq, int NOT NULL
    private String content;    // c_content, varchar(400) NOT NULL
    private Integer up;        // c_up, int NOT NULL
    private Integer down;      // c_down, int NOT NULL
    private String date;       // c_date, datetime NOT NULL
    private Integer parentSeq; // c_parent_seq, int NOT NULL

}

// CREATE TABLE `Comments` (
// 	`c_seq`	        int	            NOT NULL,
// 	`p_seq`	        int	            NOT NULL,
// 	`u_seq`	        int	            NOT NULL,
// 	`c_content`	    varchar(400)	NOT NULL,
// 	`c_up`	        int	            NOT NULL,
// 	`c_down`	    int	            NOT NULL,
// 	`c_date`	    datetime	    NOT NULL,
// 	`c_parent_seq`	int	            NOT NULL
// );

// 댓글 추천 table
// CREATE TABLE `CommentRecommendation` (
// 	`cr_seq`	int	    NOT NULL,
// 	`c_seq`	    int	    NOT NULL,
// 	`cr_like`	int 	NOT NULL,
// 	`u_seq`	    int	    NOT NULL
// );

// 삭제된 댓글 table 내용 추가해야함
// CREATE TABLE `ComemntDeleted` (
// 	`cd_seq`	int	        NOT NULL,
// 	`cd_date`	datetime	NOT NULL
// );