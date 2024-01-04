package platform.game.service.controller;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Collections;
=======
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
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
<<<<<<< HEAD
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
import platform.game.service.model.DAO.PostDAO;
=======
import jakarta.transaction.Transactional;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.service.repository",
        "platform.game.service.model" })
@RequestMapping("/board")
public class BoardController {

    @Autowired
<<<<<<< HEAD
    private PostDAO dao;

    @Autowired
=======
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
    private PostInfoRepository postInfoRepository;

    @Autowired
    private CommentInfoRepository commentInfoRepository;

    @GetMapping("/list")
<<<<<<< HEAD
    public ModelAndView list() {
        String boardCd = "board_list";
        ArrayList<Post> lists = postInfoRepository.findByBoardCd(boardCd);
        Collections.reverse(lists);
=======
    public ModelAndView list(@RequestParam("board_cd") String boardCd) {
        ArrayList<Post> lists = postInfoRepository.findByBoardCdOrderByPostIdDesc(boardCd);
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4

        String loginCheck = "true";

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            System.out.println("멤버 있음 ");
            loginCheck = "true";
        } else {
            System.out.println("멤버 없음");
            loginCheck = "false";
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list");
        modelAndView.addObject("lists", lists);
        modelAndView.addObject("loginCheck", loginCheck);
<<<<<<< HEAD
=======
        modelAndView.addObject("boardCd", boardCd);
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4

        return modelAndView;
    }

    @GetMapping("/view")
    public ModelAndView listView(@RequestParam(name = "post_id") int postId) {
        System.out.println("Controller_listView 호출");

<<<<<<< HEAD
        String loginCheck = "true";
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            loginCheck = "true";
=======
        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);

        Member member = null;
        String loginCheck = "true";
        long id = 1; // 멤버 비교를 위한 변수
        long pid = post.getMember().getMemId(); // 멤버 비교를 위한 게시물 변수
        String writePost = "false"; // 결과 보내주는 변수

        // hit 증가시키기
        post.setPostHit(post.getPostHit() + 1);
        postInfoRepository.save(post);

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            loginCheck = "true";
            id = member.getMemId();
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        } else {
            loginCheck = "false";
        }

<<<<<<< HEAD
        Post post = new Post();

        post = postInfoRepository.findByPostId(postId);
=======
        if (id == pid) {
            // 게시물 작성자가 맞을 경우
            System.out.println("맞음");
            writePost = "true";
        } else {
            // 게시물 작성자가 아닐경우
            System.out.println("틀림");
        }
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4

        // 댓글 불러오기
        ArrayList<Comment> comment = commentInfoRepository.findByPost_PostId(postId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_view");
        modelAndView.addObject("post", post);
        modelAndView.addObject("comment", comment);
        modelAndView.addObject("loginCheck", loginCheck);
<<<<<<< HEAD

=======
        modelAndView.addObject("writePost", writePost);
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        return modelAndView;
    }

    @RequestMapping("/comment_write_ok")
    public String writeComment(@RequestParam("postId") int postId, @RequestParam("ccontent") String content) {
        System.out.println("comment_write_ok 호출");
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
        } else {
            System.out.println("로그인을 하세요");
        }
        Date date = new Date();

<<<<<<< HEAD
        Comment comment = new Comment();
        comment.setPost(postInfoRepository.findByPostId(postId));
=======
        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);

        Comment comment = new Comment();
        comment.setPost(post);
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        comment.setCommentContent(content);
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setMember(member);
        System.out.println("comment 확인 : " + comment);

        commentInfoRepository.save(comment);
<<<<<<< HEAD

        // 댓글이 등록된 후에 어디로 이동할지를 결정
        // 예시: 댓글 등록 후 해당 게시물로 이동
=======
        // 댓글 카운트 추가
        post.setPostCommentCnt(post.getPostCommentCnt() + 1);
        postInfoRepository.save(post);

        // 댓글이 등록된 후에 해당 게시물로 이동
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        return "redirect:/board/view?post_id=" + postId;
    }

    @RequestMapping("/write")
<<<<<<< HEAD
    public ModelAndView listWrite() {
=======
    public ModelAndView listWrite(@RequestParam(name = "board_cd") String boardCd) {
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        System.out.println("Controller_listWrite 호출");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_write");
<<<<<<< HEAD
=======
        modelAndView.addObject("board_cd", boardCd);
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4

        return modelAndView;
    }

    @RequestMapping("/write_ok")
<<<<<<< HEAD
    public ModelAndView listWriteOk(HttpServletRequest request, Model model) {
=======
    public String listWriteOk(HttpServletRequest request, Model model) {
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        System.out.println("Controller_listWriteOk 호출");
        Post post = new Post();
        Date date = new Date();
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
<<<<<<< HEAD
        } else {
            System.out.println("로그인을 하세요");
=======
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        }
        post.setPostTitle(request.getParameter("subject"));
        post.setPostTags(request.getParameter("tags"));
        post.setPostContent(request.getParameter("content"));
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
<<<<<<< HEAD

        // 게시판 코드가 들어가는 장소
        post.setBoardCd("board_list"); // 임시로 데이터를 넣어둠
        post.setMember(member);

        int flag = dao.WriteOk(post);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_write_ok");
        modelAndView.addObject("flag", flag);

        return modelAndView;
=======
        post.setBoardCd(request.getParameter("board_cd"));
        post.setMember(member);
        post.setPostCommentCnt(0);

        int flag = 1;
        try {
            postInfoRepository.save(post);

            flag = 0;
        } catch (Exception e) {
            System.out.println("WriteOk(Post post) 오류 : " + e.getMessage());
            flag = 1;
        }

        if (flag == 0) {
            // 글쓰기 성공 시 처리
            return "redirect:../board/list?board_cd=" + request.getParameter("board_cd");
        } else {
            // 글쓰기 실패 시 처리
            return "redirect:/error";
        }
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
    }

    @GetMapping("/modify")
    public ModelAndView listModify(@RequestParam(name = "post_id") int postId) {
        System.out.println("Controller_listModift 호출");
        Post post = new Post();

        post = postInfoRepository.findByPostId(postId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_modify");
        modelAndView.addObject("post", post);

        return modelAndView;
    }

    @PostMapping("/modify_ok")
<<<<<<< HEAD
    public ModelAndView listModifyOk(@RequestParam(name = "post_id") int postId,
=======
    public String listModifyOk(@RequestParam(name = "post_id") int postId,
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
            @RequestParam(name = "subject") String title,
            @RequestParam(name = "tags") String tags,
            @RequestParam(name = "content") String content) {
        System.out.println("Controller_listModify_ok 호출");
        Date date = new Date();

        // Find the existing post by ID
        Optional<Post> optionalPost = postInfoRepository.findById(postId);
<<<<<<< HEAD
        if(optionalPost.isPresent()){
            
        }
=======

>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        Post post = optionalPost.get();
        // Update the post with the new values
        post.setPostTitle(title);
        post.setPostTags(tags);
        post.setPostContent(content);
        post.setUpdatedAt(date);

        System.out.println("modify_ok post : " + post);
<<<<<<< HEAD

        // Save the updated post
        int flag = dao.ModifyOk(post);
        System.out.println("flag : " + flag);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_modify_ok");
        modelAndView.addObject("flag", flag);

        return modelAndView;
=======
        post.getBoardCd();

        // Save the updated post
        int flag = 1;
        try {
            postInfoRepository.save(post);
            flag = 0;
        } catch (Exception e) {
            System.out.println("수정 오류 : " + e.getMessage());
            flag = 1;
        }

        System.out.println("flag : " + flag);

        if (flag == 0) {
            // 글쓰기 성공 시 처리
            return "redirect:/board/view?board_cd=" + post.getBoardCd() + "&post_id=" + postId;
        } else {
            // 글쓰기 실패 시 처리
            return "redirect:/error";
        }
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
    }

    @GetMapping("/delete")
    public ModelAndView listDelete(@RequestParam(name = "post_id") int postId) {

        System.out.println("Controller_listdelete 호출");
        Post post = new Post();

        post = postInfoRepository.findByPostId(postId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_delete");
        modelAndView.addObject("post", post);

        return modelAndView;
    }

    @PostMapping("/delete_ok")
<<<<<<< HEAD
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
=======
    @Transactional
    public String listDeleteOk(@RequestParam(name = "post_id") int postId) {

        System.out.println("Controller_listdeleteOk 호출");

        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);

        System.out.println("댓글 먼저 삭제");
        commentInfoRepository.deleteByPost_PostId(postId);
        System.out.println("게시글 삭제");
        postInfoRepository.deleteById(postId);

        return "redirect:/board/list?board_cd=" + post.getBoardCd();
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
    }
}