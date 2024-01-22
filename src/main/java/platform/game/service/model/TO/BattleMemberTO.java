package platform.game.service.model.TO;

import lombok.Data;
import platform.game.service.entity.Member;

@Data
public class BattleMemberTO{
    public BattleMemberTO(Member client){
        this.level = client.getMemLvl();
        this.nickname = client.getMemNick();
        this.win = client.getMemGameWinCnt();
        this.lose = client.getMemGameLoseCnt();
    }
    private int level;
    private String nickname;
    private int win;
    private int lose;

    // 뱃지
    // css
    // 이미지
}