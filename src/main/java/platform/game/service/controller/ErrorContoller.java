package platform.game.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class ErrorContoller {
    @RequestMapping("/")
    public ModelAndView error(){

        return new ModelAndView("error");
    }
}
