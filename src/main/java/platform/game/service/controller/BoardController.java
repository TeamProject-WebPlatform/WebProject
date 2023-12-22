package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
import platform.game.service.filter.JwtAuthFilter;
import platform.game.service.model.DAO.PostDAO;
import platform.game.service.repository.MemberInfoRepository;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.service.repository"})
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private PostDAO dao;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @GetMapping("list")
    public ModelAndView list() {
        ArrayList<Post> lists = dao.List();

        //가장 최근에 만들어진 게시글이 맨위에 나오도록 하도록 설정
        Collections.reverse(lists);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list" );
		modelAndView.addObject( "lists", lists );
	
		return modelAndView;
    }

    @GetMapping("list/view")
    public ModelAndView listView() {
        return new ModelAndView("list_view");
    }

    @RequestMapping( "list/write" )
    public ModelAndView listWrite() {
        System.out.println("Controller_listWrite 호출");

        ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list_write" );

		return modelAndView;
    }

    @RequestMapping("list/write_ok")
    public ModelAndView listWriteOk(HttpServletRequest request, Model model) {
        System.out.println("Controller_listWriteOk 호출");
        Post post = new Post();
        Date date = new Date();

        //memId 디비에서 불러오기
        String userId = jwtAuthFilter.getUserID();

        Member member = memberInfoRepository.findByMemUserid(userId)
             .orElseThrow(() -> new RuntimeException("Member not found for userId: " + userId));

        post.setPostTitle(request.getParameter( "subject" ));
        post.setPostTags(request.getParameter( "tags" ));
        post.setPostContent(request.getParameter( "content" ));
        post.setCreatedAt(date);
        post.setUpdatedAt(date);

        //게시판 코드가 들어가는 장소
        post.setBoardCd("TestBoard1");  //임시로 데이터를 넣어둠
        post.setMember(member);
        
		int flag = dao.WriteOk( post );

        ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list_write_ok" );
		modelAndView.addObject( "flag", flag );
	
		return modelAndView;
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