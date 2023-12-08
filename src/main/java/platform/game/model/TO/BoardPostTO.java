package platform.game.model.TO;

import lombok.Data;

@Data
public class BoardPostTO {
    private int p_seq;            // p_seq, int NOT NULL
    private int b_seq;            // b_seq, int NOT NULL 게시판seq
    private int u_seq;            // u_seq, int NOT NULL 유저seq
    private int t_seq;            // t_seq, int NOT NULL 태그seq
    private String subject;     // subject, varchar(100) NOT NULL
    private String content;     // content, varchar(2000) NOT NULL
    private int hit;        // hit, int NOT NULL
    private String date;        // date, datetime NOT NULL
    private String modified;    // modified, datetime NOT NULL
}
// CREATE TABLE `BoardPost` (
// 	`p_seq`	        int	            NOT NULL,
// 	`b_seq`	        int	            NOT NULL,
// 	`u_seq`	        int	            NOT NULL,
// 	`t_seq`	        int	            NOT NULL,
// 	`subject`	    varchar(100)	NOT NULL,
// 	`content`	    varchar(2000)	NOT NULL,
// 	`hit`	        int	            NOT NULL,
// 	`date`	        datetime	    NOT NULL,
// 	`modified`	    datetime	    NOT NULL
// );

// 태그 table
// CREATE TABLE `BoardTag` (
// 	`t_seq`	int	        NOT NULL,
// 	`tag`	varchar(20)	NOT NULL
// );

// 게시판 table
// CREATE TABLE `BoardInfo` (
// 	`b_seq`	    int	            NOT NULL,
// 	`name`	    varchar(100)	NOT NULL,
// 	`cat`	    varchar(20)	    NOT NULL
// );

// 게시글 추천 table
// CREATE TABLE `BoardPostRecommendation` (
// 	`pr_seq`	int	NOT NULL,
// 	`u_seq`	    int	NOT NULL,
// 	`like`	int	NOT NULL,
// 	`p_seq`	    int	NOT NULL
// );

// 삭제된 게시글 table 내용 추가해야함
// CREATE TABLE `BoardPostDeleted` (
// 	`pd_seq`	int	        NOT NULL,
// 	`date`	datetime	NOT NULL
// );