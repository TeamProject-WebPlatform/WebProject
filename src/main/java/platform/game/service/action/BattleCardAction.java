package platform.game.service.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import platform.game.service.entity.Battle;
import platform.game.service.entity.Member;
import platform.game.service.model.TO.BattlePointTO;
import platform.game.service.model.TO.BattleTO;
import platform.game.service.repository.BattleRepository;

@Component
public class BattleCardAction {
    
    @Autowired
    BattleRepository battleRepository;

    public List[] getBattleList(){
        List<BattleTO> battleTOList = new ArrayList<>();
        List<BattlePointTO> battlePointTOList = new ArrayList<>();

        List<Battle> battleList = battleRepository.findAll(); 
        for(var battle : battleList){
            BattleTO bto = new BattleTO(battle,battle.getBtPost());
            BattlePointTO pto = new BattlePointTO(battle);
            battleTOList.add(bto);
            battlePointTOList.add(pto);
        }
        
        return new List[]{battleTOList,battlePointTOList};
    }
}
