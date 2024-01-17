package platform.game.service.model.TO;

import lombok.Data;
import platform.game.service.entity.Battle;
import platform.game.service.entity.BattlePost;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;

@Data
public class BattleTO {

    public BattleTO(Battle battle,BattlePost battlePost){
        this.host = battle.getHostMember();
        this.client = battle.getClientMember();

        hostNick = host.getMemNick();
        hostWin = host.getMemGameWinCnt();
        hostLose = host.getMemGameLoseCnt();
        hostLvl= host.getMemLvl();
        hostImgName = "doyun_icon.png";
        clientNick = client.getMemNick();
        clientWin = client.getMemGameWinCnt();
        clientLose = client.getMemGameLoseCnt();
        clientLvl= client.getMemLvl();
        clientImgName = "doyun_icon.png";
        
        this.btId = battle.getBtId();
        this.point = battlePost.getBtPostPoint();

        Post post = battlePost.getPost();
        this.title = post.getPostTitle();
        this.postId = post.getPostId();
        this.gameCd = battlePost.getGameCd();
        this.state = battle.getBtState();
    }

    private Member host; // 배틀 주최자
    private Member client; // 배틀 참가자

    public String hostNick;
    public int hostWin;
    public int hostLose;
    public int hostLvl;
    public String hostImgName;
    // public String hostbadge; // 뱃지 추가할시
    
    public String clientNick;
    public int clientWin;
    public int clientLose;
    public int clientLvl;
    public String clientImgName;
    // public String clientbadge; // 뱃지 추가할시

    int btId; // 배틀 ID
    String title; // 배틀 타이틀
    int point; // 배틀 포인트
    String gameCd;
    int postId; // 해당 배틀게시글 id
    String state;
}
