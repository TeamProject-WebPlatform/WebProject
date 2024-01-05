package platform.game.service.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.Shop;
import platform.game.service.repository.ShopInfoRepository;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.service.userStats" })
public class ShopController {

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @RequestMapping("/shop")
    public ModelAndView shop() {
        ArrayList<Shop> lists = shopInfoRepository.findAll();

        System.out.println("shop 테스트"+lists);
        
        System.out.println("shop 테스트"+shopInfoRepository.findAll());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("lists", lists);
        
        return modelAndView;
    }
    
}
