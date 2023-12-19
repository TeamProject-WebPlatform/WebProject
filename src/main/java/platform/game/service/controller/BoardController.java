package platform.game.service.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ComponentScan(basePackages = { "platform.game.action" })
@RequestMapping("/board")
public class BoardController {

    @GetMapping("list")
    public ModelAndView list() {
        return new ModelAndView("list");
    }

    @GetMapping("list/view")
    public ModelAndView listView() {
        return new ModelAndView("list_view");
    }

    @GetMapping("list/write")
    public ModelAndView listWrite() {
        return new ModelAndView("list_write");
    }

    @GetMapping("list/modify")
    public ModelAndView listModify() {
        return new ModelAndView("list_modify");
    }

    @GetMapping("list/delete")
    public ModelAndView listDelete() {
        return new ModelAndView("list_delete");
    }

    @GetMapping("notice")
    public ModelAndView notice() {
        return new ModelAndView("notice_list");
    }

    @GetMapping("notice/view")
    public ModelAndView noticeView() {
        return new ModelAndView("notice_view");
    }

    @GetMapping("notice/write")
    public ModelAndView noticeWrite() {
        return new ModelAndView("notice_write");
    }

    @GetMapping("notice/modify")
    public ModelAndView noticeModify() {
        return new ModelAndView("notice_modify");
    }

    @GetMapping("notice/delete")
    public ModelAndView noticeDelete() {
        return new ModelAndView("notice_delete");
    }

    @GetMapping("fight")
    public ModelAndView fight() {
        return new ModelAndView("fight_list");
    }

    @GetMapping("fight/view")
    public ModelAndView fightView() {
        return new ModelAndView("fight_view");
    }

    @GetMapping("fight/write")
    public ModelAndView fightWrite() {
        return new ModelAndView("fight_write");
    }

    @GetMapping("fight/modify")
    public ModelAndView fightModify() {
        return new ModelAndView("fight_modify");
    }

    @GetMapping("fight/delete")
    public ModelAndView fightDelete() {
        return new ModelAndView("fight_delete");
    }

    @GetMapping("shop")
    public ModelAndView show() {
        return new ModelAndView("shop");
    }
}