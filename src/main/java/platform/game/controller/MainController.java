package platform.game.controller;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import platform.game.action.MypageAction;

@Controller
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config"})
public class MainController {

    private MypageAction mypageAction = new MypageAction();

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
    
    @GetMapping("/mypage/{userid}")
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