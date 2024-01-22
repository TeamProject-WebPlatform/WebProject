package platform.game.service.model.TO;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;
import platform.game.service.entity.Battle;
import platform.game.service.entity.MemberBetting;
import platform.game.service.repository.MemberBettingRepository;

@Data
public class BattlePointTO {
    public BattlePointTO(long memId, Battle battle){
        this.memId = memId;

        this.hostTotalPoint = battle.getBtHostMemBetPoint();
        this.clientTotalPoint = battle.getBtClientMemBetPoint();
        this.hostTotalMemNo = battle.getBtHostMemBetCnt();
        this.clientTotalMemNo = battle.getBtClientMemBetCnt();

        totalPoint = hostTotalPoint+clientTotalPoint;

        hostPointRatio = totalPoint != 0 ? (int)Math.round((hostTotalPoint*1.0/totalPoint)*100) : 0;
        clientPointRatio = totalPoint != 0 ? (int)Math.round((clientTotalPoint*1.0/totalPoint)*100) : 0;


        hostPointRewardRatio = hostTotalPoint != 0 ?  String.format("%.2f",(totalPoint*1.0f/hostTotalPoint)): "0";
        clientPointRewardRatio = clientTotalPoint != 0 ? String.format("%.2f",(totalPoint*1.0f/clientTotalPoint)) :"0";
        this.btId = battle.getBtId();
    }
    private int endpoint = 0;
    private long memId;

    private long hostTotalPoint = 0;            
    private long clientTotalPoint = 0;
    private long totalPoint;

    private int hostTotalMemNo = 0;
    private int clientTotalMemNo = 0;

    private int hostPointRatio;
    private int clientPointRatio;

    private String hostPointRewardRatio;
    private String clientPointRewardRatio;

    private int btId;
    private int alreadyBet=0; // 0은 ㄴㄴ, 1은 ㅇㅇ
    private int flag=-1; // 투표했을시 flag

    private int betSuccess; // 0은 ㄴㄴ 1은 ㅇㅇ
    private int pointReceived;
    private int pointDstb = -1;
}
