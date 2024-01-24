package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Battle;
import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberBetting;
import platform.game.service.entity.MemberProfile;
import platform.game.service.entity.PointHistory;
import platform.game.service.model.TO.MemberBettingTO;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberProfileRepository;
import platform.game.service.service.LevelService;
import platform.game.service.service.MemberInfoDetails;

@RequestMapping("/wallet")
@Controller
public class WalletController {
    @Autowired
    EntityManager entityManager;
    @Autowired
    LevelService levelService;
    @Autowired
    CommonCodeRepository commonCodeRepository;
    @Autowired
    MemberProfileRepository profileRepository;

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping("")
    public ModelAndView wallet() {
        ModelAndView mav = new ModelAndView("wallet");
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member == null) {
                return new ModelAndView("login");
            }
        }
        member = entityManager.merge(member);
        updateMemberData(member);

        List<MemberBetting> memberBettingList = member.getMemBettingList();
        List<MemberBettingTO> memberBettingTOList = new ArrayList<>();
        for (var e : memberBettingList) {
            Battle battle = e.getBattle();
            MemberBettingTO to = new MemberBettingTO(battle, battle.getBtPost().getPost(), e);
            memberBettingTOList.add(to);
        }
        MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());

        Collections.reverse(memberBettingTOList);
        mav.addObject("nickname", member.getMemNick());
        mav.addObject("memId", member.getMemId());
        mav.addObject("currentPoint", member.getMemCurPoint());
        mav.addObject("totalPoint", member.getMemTotalPoint());
        mav.addObject("memberBettingTOList", memberBettingTOList);
        mav.addObject("memberProfile", memberProfile);

        // 포인트 히스토리 정렬
        List<PointHistory> list = member.getMemPointHistoryList();
        Collections.sort(list, Collections.reverseOrder(Comparator.comparing(PointHistory::getPointHistoryId)));
        mav.addObject("memberPointHistoryList", list);

        // 레벨 디자인
        mav.addObject("memberLevel", member.getMemLvl());
        List<Integer> levelDesign = levelService.getLevelDesign();
        mav.addObject("levelDesign", levelDesign);
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        mav.addObject("totalCount", visitCount.getRemark1());
        mav.addObject("todayCount", visitCount.getRemark3());

        return mav;
    }

    @Transactional
    public void updateMemberData(Member member) {
        Hibernate.initialize(member.getMemBettingList());
        Hibernate.initialize(member.getMemPointHistoryList());
    }
}
