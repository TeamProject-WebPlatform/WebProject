package platform.game.controller;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config"})
public class MainController {

    @GetMapping("/home")
    public ModelAndView home(){
        return new ModelAndView("index");
    }
    @GetMapping("/list")
    public ModelAndView list(){
        return new ModelAndView("list");
    }
    @GetMapping("/show")
    public ModelAndView show(){
        return new ModelAndView("shop");
    }

    
}