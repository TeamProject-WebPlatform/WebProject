package platform.game.service.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.Member;
import platform.game.service.service.MemberInfoDetails;

// Spring Security의 /login 페이지 안되게
@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.security" })
public class MainController {
    @RequestMapping("/")
    public ModelAndView main() {
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
            //System.out.println(member.toString());
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
    public ModelAndView rank() {
        return new ModelAndView("rank");
    }

}