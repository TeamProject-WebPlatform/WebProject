package platform.game.service.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.Member;
import platform.game.service.entity.Shop;
import platform.game.service.repository.ShopInfoRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.service.userStats" })
public class ShopController {

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @RequestMapping("/shop")
    public ModelAndView shop() {
        
        long totalItemCount = shopInfoRepository.count();
        ArrayList<Shop> lists = shopInfoRepository.findAll();
      
        // System.out.println("shop 테스트"+lists);
        
        System.out.println("shop 테스트"+shopInfoRepository.findAll());
        
        ModelAndView modelAndView = new ModelAndView("shop");
        modelAndView.addObject("point", "0");
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getMember();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
            if (member != null) {
                modelAndView.addObject("nickname", member.getMemNick());
                modelAndView.addObject("point", member.getMemCurPoint());
            }
        } else {
            System.out.println("멤버 없음");
        }
        modelAndView.addObject("totalItemCount", totalItemCount);

        modelAndView.addObject("lists", lists);
        return modelAndView;
    }
}
