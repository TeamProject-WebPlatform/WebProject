package platform.game.service.repository;

import java.util.Date;
import java.util.List;


import platform.game.service.entity.Battle;
import platform.game.service.entity.Member;

public interface BattleCustomRepository{
    public int updateHostBetPoint(int btId, int point);
    public int updateClientBetPoint(int btId, int point);
    public int insertComment(int postId,String content, Member member);
    public int insertComment(int postId,String content, int parentCommentId, Member member);
    public int deleteComment(int commentId);
    public int reqeustBattle(long memId, int btId, int postId);
    public Object[] like(long memId, String type, int postId,int commentId, int like);
    public int[] writePost(long memId,String title, String game, String etcGame,String point,String content,Date ddDate, Date stDate);
    public int[] modifyPost(int postId, int btId, long memId,String title, String game, String etcGame,String point,String content,Date ddDate, Date stDate);
    public int deletePost(int postId, int btId);
    public int manageRequest(long requester,int isAccept,int btId);
    public void terminateBetting(int btId);
    public List<Battle> getBattleListByCondition(String selectedGame, String selectedState, String searchValue);
    public List<Battle> getBattleListByCondition(String selectedGame, String selectedState,long memId);
    public int controlBattle(int type,int btId,int postId);
    public int receivePoint(long memId, int btId,int postId);
    public int battleTerminate(Battle battle);
    public int distributePoint(int btId);
    public int receiveBettingPoint(long memId, int btId,int postId);
}
