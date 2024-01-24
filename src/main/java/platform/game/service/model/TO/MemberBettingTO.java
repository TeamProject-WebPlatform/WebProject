package platform.game.service.model.TO;

import org.hibernate.annotations.ColumnDefault;

import lombok.Data;
import platform.game.service.entity.Battle;
import platform.game.service.entity.MemberBetting;
import platform.game.service.entity.Post;

@Data
public class MemberBettingTO {
    public MemberBettingTO(Battle battle,Post post,MemberBetting memberBetting){
        this.HostMemId = battle.getHostMember().getMemId();
        this.ClientMemId = battle.getClientMember().getMemId();
        
        this.btPostTitle = battle.getBtPost().getPost().getPostTitle();
        this.betFlag = memberBetting.getBetFlag();
        this.gameCd = battle.getBtPost().getGameCd();
        this.gameName = battle.getBtPost().getEtcGameNm();
        this.betPoint = memberBetting.getBetPoint();
        this.pointReceived = memberBetting.getPointReceived();
        this.pointDstb = memberBetting.getPointDstb();

        if(post!=null) this.postId = post.getPostId();
        this.btId = battle.getBtId();
        this.state = battle.getBtState();
    }
    private Long HostMemId;
    private Long ClientMemId;

    private String btPostTitle;
    private int betFlag; //'0': 호스트 / '1': 클라이언트
    private String gameCd;
    private String gameName;
    private int betPoint;
    private String pointReceived;
    private long pointDstb;
    private int postId;
    private int btId;
    private String state;
}
