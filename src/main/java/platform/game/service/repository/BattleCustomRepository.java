package platform.game.service.repository;

import java.util.Date;

import org.attoparser.dom.Comment;

import platform.game.service.entity.Member;

public interface BattleCustomRepository{
    public int insertComment(int postId,String content, Member member);
    public int insertComment(int postId,String content, int parentCommentId, Member member);
    public int deleteComment(int commentId);
    public int reqeustBattle(long memId, int btId, int postId);
    public Object[] like(long memId, String type, int postId,int commentId, int like);
    public int[] writePost(long memId,String title, String game, String point,String content,Date ddDate, Date stDate);
}
