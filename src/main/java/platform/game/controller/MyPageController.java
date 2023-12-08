package platform.game.controller;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import platform.game.action.MypageAction;

@Controller
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config"})
@RequestMapping("/mypage")
public class MyPageController {
    
    private MypageAction mypageAction = new MypageAction();
    
    @GetMapping("/{userid}")
    public ModelAndView mypage(@PathVariable("userid") String userid, Model model){
        String markdownValueFormLocal = null;

        try {
            markdownValueFormLocal = mypageAction.getMarkdownValueFormLocal( userid );
        } catch (Exception e) {
            System.out.println( "[에러] MainController : " + e.getMessage() );
        }

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownValueFormLocal);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        model.addAttribute("contents", renderer.render(document));

        return new ModelAndView("mypage");
    }
}
