package platform.game.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.action.BattleCardAction;
import platform.game.service.entity.Battle;
import platform.game.service.entity.Member;
import platform.game.service.model.TO.BattlePointTO;
import platform.game.service.model.TO.BattleTO;
import platform.game.service.repository.BattleRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.service.action", "platform.game.service.repository",
        "platform.game.service.model" })
@RequestMapping("/battle")
public class BattleController {

    @Autowired
    BattleCardAction battleCardAction;

    @RequestMapping("")
    public ModelAndView battle(){
        ModelAndView mav = new ModelAndView("battle");
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
            }
        } else {
        }
        List[] battleList = battleCardAction.getBattleList();
        List<BattleTO> battleTOList = battleList[0];
        List<BattlePointTO> battlePointTOList = battleList[1];
        
        mav.addObject("battleTOList",battleTOList);
        mav.addObject("battlePointTOList",battlePointTOList);
        return mav;
    }
}
