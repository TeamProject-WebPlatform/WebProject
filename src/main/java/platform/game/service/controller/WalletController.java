package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Battle;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberBetting;
import platform.game.service.entity.PointHistory;
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
                    Battle battle = e.getBattle();
                    MemberBettingTO to = new MemberBettingTO(battle,battle.getBtPost().getPost(),e);
                    memberBettingTOList.add(to);
                }
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("memId",member.getMemId());
                mav.addObject("currentPoint", member.getMemCurPoint());
                mav.addObject("totalPoint", member.getMemTotalPoint());
                mav.addObject("memberBettingTOList",memberBettingTOList);
                List<PointHistory> list =member.getMemPointHistoryList();
                Collections.sort(list,Collections.reverseOrder(Comparator.comparing(PointHistory::getCreatedAt)));
                mav.addObject("memberPointHistoryList", list);
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
