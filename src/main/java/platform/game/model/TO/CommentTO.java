package platform.game.model.TO;

import lombok.Data;

@Data
public class CommentTO {
    private int c_seq;           // c_seq, int NOT NULL
    private int p_seq;           // p_seq, int NOT NULL
    private int u_seq;           // u_seq, int NOT NULL
    private String content;      // content, varchar(400) NOT NULL
    private int up;              // up, int NOT NULL
    private int down;            // down, int NOT NULL
    private String date;         // date, datetime NOT NULL
    private int parent_seq;    // parent_seq, int NOT NULL

}

// CREATE TABLE `Comments` (
// 	`c_seq`	        int	            NOT NULL,
// 	`p_seq`	        int	            NOT NULL,
// 	`u_seq`	        int	            NOT NULL,
// 	`content`	    varchar(400)	NOT NULL,
// 	`up`	        int	            NOT NULL,
// 	`down`	    int	            NOT NULL,
// 	`date`	    datetime	    NOT NULL,
// 	`parent_seq`	int	            NOT NULL
// );

// 댓글 추천 table
// CREATE TABLE `CommentRecommendation` (
// 	`cr_seq`	int	    NOT NULL,
// 	`c_seq`	    int	    NOT NULL,
// 	`like`	    int 	NOT NULL,
// 	`u_seq`	    int	    NOT NULL
// );

// 삭제된 댓글 table 내용 추가해야함
// CREATE TABLE `ComemntDeleted` (
// 	`cd_seq`	int	        NOT NULL,
// 	`date`  	datetime	NOT NULL
// );