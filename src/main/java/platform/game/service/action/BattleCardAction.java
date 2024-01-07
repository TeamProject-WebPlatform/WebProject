package platform.game.service.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import platform.game.service.entity.Member;
import platform.game.service.model.TO.BattleTO;

public class BattleCardAction {
    public List<BattleTO> getBattleList(){
        List<BattleTO> battleList = new ArrayList<>();
        Member host = Member.builder()
                        .memId(99999)
                        .memUserid("BattleMem1")
                        .memPw("123123")
                        .memNick("zl존배틀왕")
                        .memRoleCd("10003")
                        .memEmail("wsbg@naver.com")
                        .loginKindCd("19101")
                        .memCertified("Y")
                        .memCreatedAt(new Date().toString())
                        .memLvl(54)
                        .memCurPoint(30000)
                        .memTotalPoint(434535)
                        .memTotalGameCnt(300)
                        .memGameWinCnt(200)
                        .memGameLoseCnt(100)
                        .build();
        Member client = Member.builder()
                        .memId(99998)
                        .memUserid("BattleMem2")
                        .memPw("123123")
                        .memNick("가지지못한자")
                        .memRoleCd("10003")
                        .memEmail("wsbg@naver.com")
                        .loginKindCd("19101")
                        .memCertified("Y")
                        .memCreatedAt(new Date().toString())
                        .memLvl(99)
                        .memCurPoint(2)
                        .memTotalPoint(333535)
                        .memTotalGameCnt(7850)
                        .memGameWinCnt(7000)
                        .memGameLoseCnt(850)
                        .build();                
        BattleTO to = new BattleTO(host, client, "중국이 견제하고 일본에서 시샘하는 대결", 100, 0);
        battleList.add(to);
        battleList.add(to);
        battleList.add(to);
        battleList.add(to);
        battleList.add(to);
        battleList.add(to);
        battleList.add(to);
        battleList.add(to);
        battleList.add(to);
        return battleList;
    }
}
