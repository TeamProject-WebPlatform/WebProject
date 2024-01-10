package platform.game.service.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.Member;
import platform.game.service.service.MemberInfoDetails;

@RequestMapping("/wallet")
@Controller
public class WalletController {
    
    @RequestMapping("")
    public ModelAndView wallet(){
        ModelAndView mav = new ModelAndView("wallet");
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint", member.getMemCurPoint());
                mav.addObject("totalPoint", member.getMemTotalPoint());
                mav.addObject("memberBettingList",member.getMemBettingList());
            }
        } else {
            System.out.println("멤버 없음");
        }
        return mav;
    }

}
