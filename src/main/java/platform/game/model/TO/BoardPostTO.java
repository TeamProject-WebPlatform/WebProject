package platform.game.model.TO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPostTO {
    private int seq;            // p_seq, int NOT NULL
    private int bSeq;            // b_seq, int NOT NULL 게시판seq
    private int uSeq;            // u_seq, int NOT NULL 유저seq
    private int tSeq;            // t_seq, int NOT NULL 태그seq
    private String subject;     // p_subject, varchar(100) NOT NULL
    private String content;     // p_content, varchar(2000) NOT NULL
    private Integer hit;        // p_hit, int NOT NULL
    private String date;        // p_date, datetime NOT NULL
    private String modified;    // p_modified, datetime NOT NULL
}
// CREATE TABLE `BoardPost` (
// 	`p_seq`	        int	            NOT NULL,
// 	`b_seq`	        int	            NOT NULL,
// 	`u_seq`	        int	            NOT NULL,
// 	`t_seq`	        int	            NOT NULL,
// 	`p_subject`	    varchar(100)	NOT NULL,
// 	`p_content`	    varchar(2000)	NOT NULL,
// 	`p_hit`	        int	            NOT NULL,
// 	`p_date`	    datetime	    NOT NULL,
// 	`p_modified`	datetime	    NOT NULL
// );

// 태그 table
// CREATE TABLE `BoardTag` (
// 	`t_seq`	int	        NOT NULL,
// 	`tag`	varchar(20)	NOT NULL
// );

// 게시판 table
// CREATE TABLE `BoardInfo` (
// 	`b_seq`	    int	            NOT NULL,
// 	`b_name`	varchar(100)	NOT NULL,
// 	`b_cat`	    varchar(20)	    NOT NULL
// );

// 게시글 추천 table
// CREATE TABLE `BoardPostRecommendation` (
// 	`pr_seq`	int	NOT NULL,
// 	`u_seq`	    int	NOT NULL,
// 	`pr_like`	int	NOT NULL,
// 	`p_seq`	    int	NOT NULL
// );

// 삭제된 게시글 table 내용 추가해야함
// CREATE TABLE `BoardPostDeleted` (
// 	`pd_seq`	int	        NOT NULL,
// 	`pd_date`	datetime	NOT NULL
// );