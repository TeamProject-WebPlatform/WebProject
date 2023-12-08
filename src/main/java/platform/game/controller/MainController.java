package platform.game.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config"})
public class MainController {
    
    @Value("${java.file.test}") // 변수 파일에 등록된 java.file.test 값 가져오기
	String envValue;
    
    @GetMapping("/")
    public ModelAndView main(){
        return new ModelAndView("index");
    }

    @GetMapping("/list")
    public ModelAndView list(){
        return new ModelAndView("list");
    }

    @GetMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

}