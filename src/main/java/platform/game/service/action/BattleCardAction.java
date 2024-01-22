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
import platform.game.service.repository.BattleCustomRepositoryImpl;
import platform.game.service.repository.BattleRepository;
import platform.game.service.service.SendMessageService;

@Component
public class BattleCardAction {

    @Autowired
    BattleRepository battleRepository;
    @Autowired
    BattleCustomRepositoryImpl battleCustomRepositoryImpl;

    public Object[] getBattleList(long id, int page, int selectedListCnt, String selectedGame, String selectedState, String searchValue,Boolean myBattle) {
        List<Battle> battleList;
        if (myBattle) {
            battleList = battleCustomRepositoryImpl.getBattleListByCondition(selectedGame, selectedState, id);
        } else {
            battleList = battleCustomRepositoryImpl.getBattleListByCondition(selectedGame, selectedState, searchValue);
        }
        // 모든 리스트 받아왔고 page와 selectedListCnt로
        // page 1이고 10일때
        int startNum = (page - 1) * selectedListCnt;
        int endNum = page * selectedListCnt;
        if (endNum >= battleList.size())
            endNum = battleList.size();
        int lastPage = battleList.size() / selectedListCnt + (battleList.size() % selectedListCnt == 0 ? 0 : 1);
        List<Battle> targetBattleList = new ArrayList<>();
        for (int i = startNum; i < endNum; i++) {
            targetBattleList.add(battleList.get(i));
        }
        Object[] o = new Object[] { getTOList(id, targetBattleList), lastPage };
        return o;
    }

    public List[] getBattleList(long id) {
        List<Battle> battleList = battleRepository.findAll();
        return getTOList(id, battleList);
    }

    public Object[] getBattleTO(long id, int postId, int btId) {
        Optional<Battle> optionalBt = battleRepository.findByBtId(btId);
        Battle battle = null;
        if (optionalBt.isPresent()) {
            battle = optionalBt.get();
        } else {
            return null;
        }
        BattleTO bto = new BattleTO(battle, battle.getBtPost(), false);
        if (bto.getDelay() < 0 && bto.getState().equals("A")) {
            battleCustomRepositoryImpl.terminateBetting(btId);
            bto.setState("B");
        }
        BattlePointTO pto = new BattlePointTO(0, battle);

        List<MemberBetting> list = battle.getMemBettingList();
        for (int i = 0; i < list.size(); i++) {
            MemberBetting m = list.get(i);
            if (m.getMember().getMemId() == id) {
                // 이미 한거임
                pto.setAlreadyBet(1);
                pto.setFlag(m.getBetFlag());
                if (bto.getState().equals("T")) {
                    pto.setPointDstb((int) m.getPointDstb());
                    if (m.getPointDstb() == 0) {
                        // 베팅실패
                        pto.setBetSuccess(0);
                    } else if (m.getPointDstb() > 0) {
                        // 베팅성공
                        pto.setBetSuccess(1);
                        if (m.getPointReceived().equals("0")) {
                            // 미수령
                            pto.setPointReceived(0);
                        } else {
                            // 수령
                            pto.setPointReceived(1);
                        }
                    }
                }
                break;
            }
        }
        return new Object[] { bto, pto };
    }

    private List[] getTOList(long id, List<Battle> battleList) {
        List<BattleTO> battleTOList = new ArrayList<>();
        List<BattlePointTO> battlePointTOList = new ArrayList<>();
        for (var battle : battleList) {
            BattleTO bto = new BattleTO(battle, battle.getBtPost(), true);
            if (bto.getDelay() < 0 && bto.getState().equals("A")) {
                battleCustomRepositoryImpl.terminateBetting(battle.getBtId());
                bto.setState("B");
            }

            BattlePointTO pto = new BattlePointTO(0, battle);

            // 베팅 중복 체크
            List<MemberBetting> list = battle.getMemBettingList();
            for (int i = 0; i < list.size(); i++) {
                MemberBetting m = list.get(i);
                if (m.getMember().getMemId() == id) {
                    // 이미 한거임
                    pto.setAlreadyBet(1);
                    pto.setFlag(m.getBetFlag());
                    if (bto.getState().equals("T")) {
                        pto.setPointDstb((int) m.getPointDstb());
                        if (m.getPointDstb() == 0) {
                            // 베팅실패
                            pto.setBetSuccess(0);
                        } else if (m.getPointDstb() > 0) {
                            // 베팅성공
                            pto.setBetSuccess(1);
                            if (m.getPointReceived().equals("0")) {
                                // 미수령
                                pto.setPointReceived(0);
                            } else {
                                // 수령
                                pto.setPointReceived(1);
                            }
                        }
                    }
                    System.out.println(pto.toString());
                }
            }

            battleTOList.add(bto);
            battlePointTOList.add(pto);
        }
        return new List[] { battleTOList, battlePointTOList };
    }
}
