package platform.game.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ComponentScan(basePackages = {"platform.game.action"})
//@GetMapping("/board")
public class BoardController {
    //영진 브랜치 테스트
    @GetMapping("/asd")
    public ModelAndView main(){
        return new ModelAndView("index");
    }

}