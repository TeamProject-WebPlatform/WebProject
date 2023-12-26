package platform.game.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nimbusds.jose.shaded.gson.JsonArray;

import platform.game.service.entity.Member;
import platform.game.service.entity.RankList;
import platform.game.service.mapper.SqlMapperInter;
import platform.game.service.model.DAO.RankDAO;
import platform.game.service.model.TO.LevelRankTO;
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
    public ModelAndView main() {
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            System.out.println("멤버 있음 " + member.toString());
        } else {
            System.out.println("멤버 없음");
        }
        return new ModelAndView("index");

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
        List<LevelRankTO> lists = sqlMapperInter.getLevelrank();
        ArrayList<Integer> ranks = rankDAO.getLevelList();
        modelAndView.setViewName("rank");
        modelAndView.addObject("lists", lists);
        modelAndView.addObject("ranks", ranks);

        return modelAndView;
    }

}