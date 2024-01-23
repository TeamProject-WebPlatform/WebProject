package platform.game.service.model.TO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Data;
import platform.game.service.entity.Battle;
import platform.game.service.entity.BattlePost;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberBetting;
import platform.game.service.entity.Post;
import platform.game.service.repository.MemberBettingRepository;
import platform.game.service.service.SendMessageService;

@Data
public class BattleTO {
    public BattleTO(Battle battle,BattlePost battlePost,boolean isList){
        this.host = battle.getHostMember();
        this.client = battle.getClientMember();

        hostNick = host.getMemNick();
        hostWin = host.getMemGameWinCnt();
        hostLose = host.getMemGameLoseCnt();
        hostLvl= host.getMemLvl();
        hostImgName = "doyun_icon.png";
        if(client!=null){
            clientNick = client.getMemNick();
            clientWin = client.getMemGameWinCnt();
            clientLose = client.getMemGameLoseCnt();
            clientLvl= client.getMemLvl();
            clientImgName = "doyun_icon.png";
        }
        
        this.btId = battle.getBtId();
        this.point = battlePost.getBtPostPoint();
        this.pointReceived = battlePost.getBtPostPointReceived();

        Post post = battlePost.getPost();
        this.title = post.getPostTitle();
        this.postId = post.getPostId();
        this.gameCd = battlePost.getGameCd();
        this.etcGame = battlePost.getEtcGameNm()==null?"":battlePost.getEtcGameNm();
        this.state = battle.getBtState();
        this.result = battle.getBtResult();
        if(!isList){
            this.deadlineDt = battlePost.getBtPostDeadLine();
            this.startDt = battlePost.getBtStartDt();
            this.btEndDt = battle.getBtEndDt();
            this.applicantsString = battlePost.getBtPostApplicants();
            applicants = splitApplicants(applicantsString);
        }
        if(this.state.equals("A")) this.delay = battlePost.getBettingFinTime()-new Date().getTime();
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
    String pointReceived;
    String gameCd;
    String etcGame;
    int postId; // 해당 배틀게시글 id
    String state;
    String result;
    Date deadlineDt;
    Date startDt;
    Date btEndDt;
    long delay;

    String applicantsString;// 신청자들 memId,보류상태,신청시간/memId,보류상태
    String[][] applicants;
    public String[][] splitApplicants(String str){
        System.out.println(str);
        if(str==null || str.equals("")){
            return null;
        }
        String[] s = str.split("/");
        if(s.length==0) return null;
        String[][] res = new String[s.length][3];
        
        for(int i = 0;i<s.length;i++){
            String[] tmp = s[i].split(",");
            res[i][0] = tmp[0];
            res[i][1] = tmp[1];
            res[i][2] = tmp[2];
        }
        return res;
    }
}

