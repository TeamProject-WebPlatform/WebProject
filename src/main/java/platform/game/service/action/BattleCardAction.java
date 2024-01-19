package platform.game.service.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.javassist.compiler.ast.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import platform.game.service.entity.Battle;
import platform.game.service.entity.MemberBetting;
import platform.game.service.model.TO.BattlePointTO;
import platform.game.service.model.TO.BattleTO;
import platform.game.service.repository.BattleRepository;

@Component
public class BattleCardAction {

    @Autowired
    BattleRepository battleRepository;

    public List[] getBattleList(long id) {
        List<BattleTO> battleTOList = new ArrayList<>();
        List<BattlePointTO> battlePointTOList = new ArrayList<>();

        List<Battle> battleList = battleRepository.findAll();
        for (var battle : battleList) {

            BattleTO bto = new BattleTO(battle, battle.getBtPost(),true);
            BattlePointTO pto = new BattlePointTO(0,battle);

            // 베팅 중복 체크
            List<MemberBetting> list = battle.getMemBettingList();
            for (int i = 0; i < list.size(); i++) {
                MemberBetting m = list.get(i);
                if (m.getMember().getMemId() == id) {
                    // 이미 한거임
                    pto.setAlreadyBet(1);
                    pto.setFlag(m.getBetFlag());
                }
            }

            battleTOList.add(bto);
            battlePointTOList.add(pto);
        }

        return new List[] { battleTOList, battlePointTOList };
    }

    public Object[] getBattleTO(long id, int postId, int btId) {
        Optional<Battle> optionalBt = battleRepository.findByBtId(btId);
        Battle battle = null;
        if (optionalBt.isPresent()) {
            battle = optionalBt.get();
        } else {
            return null;
        }
        BattleTO bto = new BattleTO(battle, battle.getBtPost(),false);
        BattlePointTO pto = new BattlePointTO(0,battle);

        List<MemberBetting> list = battle.getMemBettingList();
        for (int i = 0; i < list.size(); i++) {
            MemberBetting m = list.get(i);
            if (m.getMember().getMemId() == id) {
                // 이미 한거임
                pto.setAlreadyBet(1);
                pto.setFlag(m.getBetFlag());
                break;
            }
        }
        return new Object[] {bto,pto};
    }
}
