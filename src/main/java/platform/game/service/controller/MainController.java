package platform.game.service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.Member;
import platform.game.service.mapper.SqlMapperInter;
import platform.game.service.model.DAO.RankDAO;
import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.model.TO.PointRankTO;
import platform.game.service.model.TO.RollingRankTO;
import platform.game.service.model.TO.WinRankTO;
import platform.game.service.service.MemberInfoDetails;

// Spring Security의 /login 페이지 안되게
@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.security" })
public class MainController {
    @Autowired
    SqlMapperInter sqlMapperInter;

    @Autowired
    RankDAO rankDAO;

    @RequestMapping("/")
    public ModelAndView main(ModelAndView modelAndView) {
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            System.out.println("멤버 있음 " + member.toString());
        } else {
            System.out.println("멤버 없음");
        }

        List<RollingRankTO> rol = sqlMapperInter.getRol();
        modelAndView.setViewName("index");
        modelAndView.addObject("rol", rol);

        return modelAndView;

    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("list");
    }

    @GetMapping("/show")
    public ModelAndView show() {
        return new ModelAndView("shop");
    }

    @GetMapping("/rank")
    public ModelAndView rank(ModelAndView modelAndView) {
        List<WinRankTO> WinRanklists = sqlMapperInter.getWinrank();
        List<LevelRankTO> LevelRanklists = sqlMapperInter.getLevelrank();
        List<PointRankTO> PointRanklists = sqlMapperInter.getPointrank();

        List<Integer> WinRanks = rankDAO.getWinList();
        List<Integer> LevelLists = rankDAO.getLevelList();
        List<Integer> PointRanks = rankDAO.getPointList();

        modelAndView.setViewName("rank");
        modelAndView.addObject("winlist", WinRanklists);
        modelAndView.addObject("levellist", LevelRanklists);
        modelAndView.addObject("pointlist", PointRanklists);

        modelAndView.addObject("levels", LevelLists);
        modelAndView.addObject("winrank", WinRanks);
        modelAndView.addObject("pointrank", PointRanks);

        return modelAndView;
    }

}