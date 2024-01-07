package platform.game.service.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.Member;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.security" })
@RequestMapping("/profile")
public class ProfileController {

    @RequestMapping("")
    public ModelAndView profilepage() {
        return new ModelAndView("profile");
    }

    @PostMapping("/{userid}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView profile(@PathVariable("userid") String userid, Model model) {
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        model.addAttribute("member", member);

        return new ModelAndView("profile");
    }

}
