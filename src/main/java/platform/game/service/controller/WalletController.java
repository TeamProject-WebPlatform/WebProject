package platform.game.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberBetting;
import platform.game.service.model.TO.MemberBettingTO;
import platform.game.service.service.MemberInfoDetails;

@RequestMapping("/wallet")
@Controller
public class WalletController {
    @Autowired
    EntityManager entityManager;

    @Transactional
    @RequestMapping("")
    public ModelAndView wallet(){
        ModelAndView mav = new ModelAndView("wallet");
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                member = entityManager.merge(member);
                updateMemberData(member);

                List<MemberBetting> memberBettingList = member.getMemBettingList();
                List<MemberBettingTO> memberBettingTOList = new ArrayList<>();
                for(var e : memberBettingList){
                    e.getBattle().getBtId();
                    e.getBattle().getBtPost();
                }
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint", member.getMemCurPoint());
                mav.addObject("totalPoint", member.getMemTotalPoint());
                mav.addObject("memberBettingList",member.getMemBettingList());
                mav.addObject("memberPointHistoryList", member.getMemPointHistoryList());
            }
        } else {
            System.out.println("멤버 없음");
        }
        return mav;
    }

    @Transactional
    public void updateMemberData(Member member) {
        Hibernate.initialize(member.getMemBettingList());
        Hibernate.initialize(member.getMemPointHistoryList());
    }
}
