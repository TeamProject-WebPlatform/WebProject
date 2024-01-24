package platform.game.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberProfile;
import platform.game.service.mapper.SqlMapperInter;
import platform.game.service.model.DAO.RankDAO;
import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.model.TO.PointRankTO;
import platform.game.service.model.TO.WinRankTO;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberFavoriteGameRepository;
import platform.game.service.repository.MemberProfileRepository;
import platform.game.service.repository.RankingRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.security" })
public class RankController {
    @Autowired
    SqlMapperInter sqlMapperInter;

    @Autowired
    RankDAO rankDAO;

    @Autowired
    RankingRepository rankingRepository;

    @Autowired
    MemberFavoriteGameRepository memberFavoriteGameRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Autowired
    MemberProfileRepository profileRepository;

    @GetMapping("/getRankFragment")
    public String getRankFragment(@RequestParam("board_cd") String boardCd, Model model) {
        return "fragments/content/rank";
    }

    @PostMapping("/getLevelChart")
    @ResponseBody
    public List<Integer> getLevelChart() {
        List<Integer> LevelChart = rankDAO.getLevelList();
        return LevelChart;
    }

    @PostMapping("/getWinRateChart")
    @ResponseBody
    public List<Integer> getWinRateChart() {
        List<Integer> WinRateChart = rankDAO.getWinList();
        return WinRateChart;
    }

    @PostMapping("/getPointChart")
    @ResponseBody
    public List<Integer> getPointChart() {
        List<Integer> PointChart = rankDAO.getPointList();
        return PointChart;
    }

    @RequestMapping({ "/levelrank", "/levelrank/{game}" })
    public ModelAndView LevelRank(@PathVariable(name = "game", required = false) String game) {
        String navRank = "nav-levelrank";

        if (game == null) {
            game = "";
        }

        String game_cd = "";

        switch (game) {
            case "lol":
                game_cd = "30001";
                break;
            case "battleground":
                game_cd = "30002";
                break;
            case "overwatch":
                game_cd = "30003";
                break;
            case "valorant":
                game_cd = "30004";
                break;
            case "fifa":
                game_cd = "30005";
                break;
            case "steam":
                game_cd = "30006";
                break;
            default:
                game_cd = "";
                break;
        }

        List<String[]> getRankerBadge = new ArrayList<String[]>();

        ModelAndView mav = new ModelAndView("levelrank");
        if (game_cd == "") {
            List<LevelRankTO> getLevelTable = sqlMapperInter.getLevelRank();
            List<Object[]> getRankerProfile = rankingRepository.getLevelRankerProfile("0");
            List<String> getRankerBadgeList = rankingRepository.getLevelRankerBadgeList("0");

            for(int i=0;i<getRankerBadgeList.size();i++){
                String[] badge = getRankerBadgeList.get(i).split(", ");
                getRankerBadge.add(badge);
            }

            mav.addObject("level", getLevelTable);
            mav.addObject("profile", getRankerProfile);
            mav.addObject("badge", getRankerBadge);
        } else {
            List<LevelRankTO> getOtherLevelTable = sqlMapperInter.getOtherLevelRank(game_cd);
            List<Object[]> getOtherRankerProfile = rankingRepository.getLevelRankerProfile(game_cd);
            mav.addObject("otherlevel", getOtherLevelTable);
            mav.addObject("otherprofile", getOtherRankerProfile);
        }

        mav.addObject("navRank", navRank);

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint", member.getMemCurPoint());
                mav.addObject("memId", member.getMemId());
                mav.addObject("memberProfile", memberProfile);
            }
        } else {
            System.out.println("멤버 없음");
        }

        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        mav.addObject("totalCount", visitCount.getRemark1());
        mav.addObject("todayCount", visitCount.getRemark3());

        return mav;
    }

    @RequestMapping({ "/winraterank", "/winraterank/{game}" })
    public ModelAndView WinRateRank(@PathVariable(name = "game", required = false) String game) {
        String navRank = "nav-winraterank";

        if (game == null) {
            game = "";
        }

        String game_cd = "";

        switch (game) {
            case "lol":
                game_cd = "30001";
                break;
            case "battleground":
                game_cd = "30002";
                break;
            case "overwatch":
                game_cd = "30003";
                break;
            case "valorant":
                game_cd = "30004";
                break;
            case "fifa":
                game_cd = "30005";
                break;
            default:
                game_cd = "";
                break;
        }

        List<String[]> getRankerBadge = new ArrayList<String[]>();

        ModelAndView mav = new ModelAndView("winraterank");
        if (game_cd == "") {
            List<WinRankTO> getWinRateTable = sqlMapperInter.getWinRateRank();
            List<Object[]> getRankerProfile = rankingRepository.getWinRateRankerProfile("0");
            List<String> getRankerBadgeList = rankingRepository.getWinRateRankerBadgeList("0");

            for(int i=0;i<getRankerBadgeList.size();i++){
                String[] badge = getRankerBadgeList.get(i).split(", ");
                getRankerBadge.add(badge);
            }

            mav.addObject("win", getWinRateTable);
            mav.addObject("profile", getRankerProfile);
            mav.addObject("badge", getRankerBadge);
        } else {
            List<WinRankTO> getOtherWinRateTable = sqlMapperInter.getOtherWinRateRank(game_cd);
            List<Object[]> getOtherRankerProfile = rankingRepository.getWinRateRankerProfile(game_cd);
            mav.addObject("otherwin", getOtherWinRateTable);
            mav.addObject("otherprofile", getOtherRankerProfile);
        }

        mav.addObject("navRank", navRank);

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint", member.getMemCurPoint());
                mav.addObject("memId", member.getMemId());
                mav.addObject("memberProfile", memberProfile);
            }
        } else {
            System.out.println("멤버 없음");
        }
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        mav.addObject("totalCount", visitCount.getRemark1());
        mav.addObject("todayCount", visitCount.getRemark3());

        return mav;
    }

    @RequestMapping({ "/pointrank", "/pointrank/{game}" })
    public ModelAndView PointRank(@PathVariable(name = "game", required = false) String game) {
        String navRank = "nav-pointrank";

        if (game == null) {
            game = "";
        }

        String game_cd = "";

        switch (game) {
            case "lol":
                game_cd = "30001";
                break;
            case "battleground":
                game_cd = "30002";
                break;
            case "overwatch":
                game_cd = "30003";
                break;
            case "valorant":
                game_cd = "30004";
                break;
            case "fifa":
                game_cd = "30005";
                break;
            default:
                game_cd = "";
                break;
        }

        List<String[]> getRankerBadge = new ArrayList<String[]>();

        ModelAndView mav = new ModelAndView("pointrank");
        if (game_cd == "") {
            List<PointRankTO> getPointTable = sqlMapperInter.getPointRank();
            List<Object[]> getRankerProfile = rankingRepository.getPointRankerProfile("0");
            List<String> getRankerBadgeList = rankingRepository.getPointRankerBadgeList("0");

            for(int i=0;i<getRankerBadgeList.size();i++){
                String[] badge = getRankerBadgeList.get(i).split(", ");
                getRankerBadge.add(badge);
            }

            mav.addObject("point", getPointTable);
            mav.addObject("profile", getRankerProfile);
            mav.addObject("badge", getRankerBadge);
            
        } else {
            List<PointRankTO> getOtherPointTable = sqlMapperInter.getOtherPointRank(game_cd);
            List<Object[]> getOtherRankerProfile = rankingRepository.getPointRankerProfile(game_cd);
            mav.addObject("otherpoint", getOtherPointTable);
            mav.addObject("otherprofile", getOtherRankerProfile);
        }

        mav.addObject("navRank", navRank);

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint", member.getMemCurPoint());
                mav.addObject("memId", member.getMemId());
                mav.addObject("memberProfile", memberProfile);
            }
        } else {
            System.out.println("멤버 없음");
        }
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        mav.addObject("totalCount", visitCount.getRemark1());
        mav.addObject("todayCount", visitCount.getRemark3());

        return mav;
    }
}
