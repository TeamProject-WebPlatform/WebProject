package platform.game.service.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ComponentScan(basePackages = { "platform.game.action" })
@RequestMapping("/servicecenter")
public class ServiceCenterController {

    @RequestMapping("main")
    public ModelAndView servicemain() {
        return new ModelAndView("service_main");
    }
}
