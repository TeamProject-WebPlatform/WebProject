package platform.game.service.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
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

import platform.game.service.entity.Member;
import platform.game.service.entity.MemberFavoriteGame;
import platform.game.service.mapper.SqlMapperInter;
import platform.game.service.model.DAO.RankDAO;
import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.model.TO.PointRankTO;
import platform.game.service.model.TO.RollingRankTO;
import platform.game.service.model.TO.WinRankTO;
import platform.game.service.repository.MemberFavoriteGameRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.security" })
public class RankController {
    @Autowired
    SqlMapperInter sqlMapperInter;

    @Autowired
    RankDAO rankDAO;

    @Autowired
    MemberFavoriteGameRepository memberFavoriteGameRepository;

    private List<RollingRankTO> rollingRankList;

    public RankController(SqlMapperInter sqlMapperInter) {
        this.sqlMapperInter = sqlMapperInter;
        rollingRankList = sqlMapperInter.getRol();
    }

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

        ModelAndView mav = new ModelAndView("levelrank");
        if (game_cd == "") {
            List<LevelRankTO> getLevelTable = sqlMapperInter.getLevelRank();
            mav.addObject("level", getLevelTable);
        } else {
            List<LevelRankTO> LevelTable = sqlMapperInter.getOtherLevelRank(game_cd);
            mav.addObject("otherlevel", LevelTable);
        }

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
            }
        } else {
            System.out.println("멤버 없음");
        }

        return mav;
    }

    @RequestMapping({ "/winraterank", "/winraterank/{game}" })
    public ModelAndView WinRateRank(@PathVariable(name = "game", required = false) String game) {

        ModelAndView mav = new ModelAndView("winraterank");

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

        List<WinRankTO> getWinRateTable = sqlMapperInter.getWinRateRank();
        mav.addObject("win", getWinRateTable);

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
            }
        } else {
            System.out.println("멤버 없음");
        }

        return mav;
    }

    @RequestMapping({ "/pointrank", "/pointrank/{game}" })
    public ModelAndView PointRank(@PathVariable(name = "game", required = false) String game) {

        ModelAndView mav = new ModelAndView("pointrank");

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

        List<PointRankTO> getPointTable = sqlMapperInter.getPointRank();
        mav.addObject("point", getPointTable);

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
            }
        } else {
            System.out.println("멤버 없음");
        }

        return mav;
    }
}
