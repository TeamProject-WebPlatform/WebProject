package platform.game.controller;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

// Spring Security의 /login 페이지 안되게
@Controller
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config","platform.game.security"})
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