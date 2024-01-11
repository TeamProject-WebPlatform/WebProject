package platform.game.service.model.TO;

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
        this.betPoint = memberBetting.getBetPoint();
    }
    private Long HostMemId;
    private Long ClientMemId;

    private String btPostTitle;
    private int betFlag; //'0': 호스트 / '1': 클라이언트
    private int betPoint;
}
