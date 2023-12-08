package platform.game.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ComponentScan(basePackages = {"platform.game.action"})
@RequestMapping("/board")
public class BoardController {

    @GetMapping("list")
    public ModelAndView list(){
        return new ModelAndView("list");
    }
    @GetMapping("notice")
    public ModelAndView notice(){
        return new ModelAndView("list");
    }
    @GetMapping("fight")
    public ModelAndView fight(){
        return new ModelAndView("list");
    }
}