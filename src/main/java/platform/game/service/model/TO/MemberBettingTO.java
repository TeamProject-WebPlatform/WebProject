package platform.game.service.model.TO;

import lombok.Data;
import platform.game.service.entity.Battle;

@Data
public class MemberBettingTO {
    public MemberBettingTO(Battle battle){
        this.HostMemId = battle.getHostMember().getMemId();
        this.ClientMemId = battle.getClientMember().getMemId();
        this.btPostTitle = battle.getBtPost().getPost().getPostTitle();
        
    }
    private Long HostMemId;
    private Long ClientMemId;

    private String btPostTitle;
    private int betFlag; //'0': 호스트 / '1': 클라이언트
    private int betPoint;
}
