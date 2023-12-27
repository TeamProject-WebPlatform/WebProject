package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
import platform.game.service.model.DAO.PostDAO;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.service.repository", "platform.game.service.model"})
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private PostDAO dao;

    @Autowired
    private PostInfoRepository postInfoRepository;

    @Autowired
    private CommentInfoRepository commentInfoRepository;


    @GetMapping("/list")
    public ModelAndView list() {
        String boardCd = "board_list";
        ArrayList<Post> lists = postInfoRepository.findByBoardCd(boardCd);
        Collections.reverse(lists);
		
        String loginCheck = "true";

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            System.out.println("멤버 있음 ");
            loginCheck = "true";
        }else{
            System.out.println("멤버 없음");
            loginCheck = "false";
        }

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list" );
		modelAndView.addObject( "lists", lists );
        modelAndView.addObject( "loginCheck", loginCheck );

		return modelAndView;
    }

    @GetMapping("/view")
    public ModelAndView listView(@RequestParam(name = "post_id") int postId) {
        System.out.println("Controller_listView 호출");

        String loginCheck = "true";
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            loginCheck = "true";
        }else{
            loginCheck = "false";
        }

        Post post = new Post();
        
        post = postInfoRepository.findByPostId(postId);

        //댓글 불러오기
        ArrayList<Comment> comment = commentInfoRepository.findByPost_PostId(postId);

        ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list_view" );
		modelAndView.addObject( "post", post );
        modelAndView.addObject( "comment", comment );
        modelAndView.addObject( "loginCheck", loginCheck );
	
		return modelAndView;
    }

    @RequestMapping("/comment_write_ok")
    public String writeComment(@RequestParam("postId") int postId, @RequestParam("ccontent") String content) {
        System.out.println("comment_write_ok 호출");
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
        }else{
            System.out.println("로그인을 하세요");
        }
        Date date = new Date();

        Comment comment = new Comment(); 
        comment.setPost(postInfoRepository.findByPostId(postId));
        comment.setCommentContent(content);
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setMember(member);
        System.out.println("comment 확인 : " + comment);

        commentInfoRepository.save(comment);


        // 댓글이 등록된 후에 어디로 이동할지를 결정
        // 예시: 댓글 등록 후 해당 게시물로 이동
        return "redirect:/board/view?post_id=" + postId;
    }

    @RequestMapping( "/write" )
    public ModelAndView listWrite() {
        System.out.println("Controller_listWrite 호출");

        ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list_write" );

		return modelAndView;
    }

    @RequestMapping("/write_ok")
    public ModelAndView listWriteOk(HttpServletRequest request, Model model) {
        System.out.println("Controller_listWriteOk 호출");
        Post post = new Post();
        Date date = new Date();
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
        }else{
            System.out.println("로그인을 하세요");
        }
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

    @GetMapping("/modify")
    public ModelAndView listModify(@RequestParam(name = "post_id") int postId) {
        System.out.println("Controller_listModift 호출");
        Post post = new Post();
        
        post = postInfoRepository.findByPostId(postId);

        ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list_modify" );
		modelAndView.addObject( "post", post );

        return modelAndView;
    }

    @PostMapping("/modify_ok")
    public ModelAndView listModifyOk(@RequestParam(name = "post_id") int postId, 
                                  @RequestParam(name = "subject") String title, 
                                  @RequestParam(name = "tags") String tags, 
                                  @RequestParam(name = "content") String content) {
        System.out.println("Controller_listModify_ok 호출");
        Date date = new Date();

        // Find the existing post by ID
        Optional<Post> optionalPost = postInfoRepository.findById(postId);
        
        Post post = optionalPost.get();
        // Update the post with the new values
        post.setPostTitle(title);
        post.setPostTags(tags);
        post.setPostContent(content);
        post.setUpdatedAt(date);

        System.out.println("modify_ok post : " + post);

            // Save the updated post
        int flag = dao.ModifyOk(post);
        System.out.println("flag : " + flag);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_modify_ok");
        modelAndView.addObject( "flag", flag );

        return modelAndView;
    }


    @GetMapping("/delete")
    public ModelAndView listDelete(@RequestParam(name = "post_id") int postId) {

        System.out.println("Controller_listdelete 호출");
        Post post = new Post();
        
        post = postInfoRepository.findByPostId(postId);

        ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list_delete" );
		modelAndView.addObject( "post", post );

        return modelAndView;
    }

    @PostMapping("/delete_ok")
    public ModelAndView listDeleteOk(@RequestParam(name = "post_id") int postId) {

        System.out.println("Controller_listdeleteOk 호출");

        // postInfoRepository.deleteById(postId)를 사용하여 게시물 삭제
        postInfoRepository.deleteById(postId);

        // flag 값 추가
        int flag = 0;

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_delete_ok");

        // flag를 ModelAndView에 추가
        modelAndView.addObject("flag", flag);
        return modelAndView;
    }

    @GetMapping("/notice")
    public ModelAndView noticelist() {
        String boardCd = "notice";
        ArrayList<Post> lists = postInfoRepository.findByBoardCd(boardCd);
        //가장 최근에 만들어진 게시글이 맨위에 나오도록 하도록 설정
        Collections.reverse(lists);
		
        String loginCheck = "true";

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            System.out.println("멤버 있음 ");
            loginCheck = "true";
        }else{
            System.out.println("멤버 없음");
            loginCheck = "false";
        }

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "list" );
		modelAndView.addObject( "lists", lists );
        modelAndView.addObject( "loginCheck", loginCheck );

		return modelAndView;
    }

    @GetMapping("/notice/view")
    public ModelAndView noticeView() {
        return new ModelAndView("notice_view");
    }

    @GetMapping("/notice/write")
    public ModelAndView noticeWrite() {
        return new ModelAndView("notice_write");
    }

    @GetMapping("/notice/modify")
    public ModelAndView noticeModify() {
        return new ModelAndView("notice_modify");
    }

    @GetMapping("/notice/delete")
    public ModelAndView noticeDelete() {
        return new ModelAndView("notice_delete");
    }

    @GetMapping("/fight")
    public ModelAndView fight() {
        return new ModelAndView("fight_list");
    }

    @GetMapping("/fight/view")
    public ModelAndView fightView() {
        return new ModelAndView("fight_view");
    }

    @GetMapping("fight/write")
    public ModelAndView fightWrite() {
        return new ModelAndView("fight_write");
    }

    @GetMapping("/fight/modify")
    public ModelAndView fightModify() {
        return new ModelAndView("fight_modify");
    }

    @GetMapping("/fight/delete")
    public ModelAndView fightDelete() {
        return new ModelAndView("fight_delete");
    }

    @GetMapping("/shop")
    public ModelAndView show() {
        return new ModelAndView("shop");
    }
}