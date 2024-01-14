package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import jakarta.transaction.Transactional;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
import platform.game.service.model.TO.BoardCpageTO;
import platform.game.service.model.TO.CommentTO;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.service.repository",
        "platform.game.service.model" })
// @RequestMapping("/board")
public class BoardController {

    @Autowired
    private PostInfoRepository postInfoRepository;

    @Autowired
    private CommentInfoRepository commentInfoRepository;

    // @RequestMapping("/shop")
    // public String shop(){
    // return "shop";
    // }
    @GetMapping("/list")
    public ModelAndView list(@RequestParam("board_cd") String boardCd, HttpServletRequest request) {
        ArrayList<Post> lists = postInfoRepository.findByBoardCdOrderByPostIdDesc(boardCd);

        String boardCd_name = "Notice";
        String navBoard = "nav-";
        switch (boardCd) {
            case "20001":
                boardCd_name = "Notice";
                navBoard = navBoard + "notice";
                break;
            case "20002":
                boardCd_name = "Update";
                navBoard = navBoard + "update";
                break;
            case "20003":
                boardCd_name = "Event";
                navBoard = navBoard + "event";
                break;
            case "20004":
                boardCd_name = "Free Board";
                navBoard = navBoard + "free";
                break;
            case "20005":
                boardCd_name = "Sharing information";
                navBoard = navBoard + "information";
                break;
            case "20006":
                boardCd_name = "Share the strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        String loginCheck = "true";

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            System.out.println("멤버 있음 ");
            loginCheck = "true";
        } else {
            System.out.println("멤버 없음");
            loginCheck = "false";
        }

        // cpage 작업
        BoardCpageTO cpageTO = new BoardCpageTO();
        int cpage = 1;
        int recordPerPage = cpageTO.getRecordPerPage();
        int blockPerPage = cpageTO.getBlockPerPage();
        if (request.getParameter("cpage") != null && !request.getParameter("cpage").equals("")) {
            cpage = Integer.parseInt(request.getParameter("cpage"));
        }

        cpageTO.setCpage(cpage);
        cpageTO.setTotalRecord(lists.size());
        cpageTO.setTotalPage(((cpageTO.getTotalRecord() - 1) / recordPerPage) + 1);
        cpageTO.setStartBlock(cpage - (cpage - 1) % blockPerPage);
        cpageTO.setEndBlock(cpage - (cpage - 1) % blockPerPage + blockPerPage - 1);
        if (cpageTO.getEndBlock() >= cpageTO.getTotalPage()) {
            cpageTO.setEndBlock(cpageTO.getTotalPage());
        }

        // SKIP - 각 페이지 별 게시글 자르기
        int skip = (cpage - 1) * cpageTO.getRecordPerPage();
        int startIndex = Math.min(skip, lists.size());
        int endIndex = Math.min(startIndex + cpageTO.getRecordPerPage(), lists.size());
        lists = new ArrayList<>(lists.subList(startIndex, endIndex));

        cpageTO.setBoardLists(lists);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("board_list");
        modelAndView.addObject("lists", lists);
        modelAndView.addObject("loginCheck", loginCheck);
        modelAndView.addObject("boardCd", boardCd);
        modelAndView.addObject("cpage", cpageTO);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);

        return modelAndView;
    }

    @GetMapping("/view")
    public ModelAndView listView(@RequestParam("board_cd") int boardCd,
            @RequestParam("post_id") int postId,
            @RequestParam("cpage") int cpage) {
        System.out.println("Controller_listView 호출");

        String boardCd_name = "Notice";
        String navBoard = "nav-";
        switch (boardCd) {
            case 20001:
                boardCd_name = "Notice";
                navBoard = navBoard + "notice";
                break;
            case 20002:
                boardCd_name = "Update";
                navBoard = navBoard + "update";
                break;
            case 20003:
                boardCd_name = "Event";
                navBoard = navBoard + "event";
                break;
            case 20004:
                boardCd_name = "Free Board";
                navBoard = navBoard + "free";
                break;
            case 20005:
                boardCd_name = "Sharing information";
                navBoard = navBoard + "information";
                break;
            case 20006:
                boardCd_name = "Share the strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

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
        } else {
            loginCheck = "false";
        }

        if (id == pid) {
            // 게시물 작성자가 맞을 경우
            System.out.println("맞음");
            writePost = "true";
        } else {
            // 게시물 작성자가 아닐경우
            System.out.println("틀림");
        }

        // 댓글 불러오기
        ArrayList<Comment> comment = commentInfoRepository.findByPost_PostId(postId);
        ArrayList<CommentTO> commentTree = buildCommentTree(comment);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("board_view");
        modelAndView.addObject("post", post);
        modelAndView.addObject("comment", comment);
        modelAndView.addObject("loginCheck", loginCheck);
        modelAndView.addObject("writePost", writePost);
        modelAndView.addObject("cpage", cpage);
        modelAndView.addObject("board_cd", boardCd);
        modelAndView.addObject("commentTree", commentTree);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);
        return modelAndView;
    }

    @RequestMapping("/write")
    public ModelAndView listWrite(@RequestParam(name = "board_cd") String boardCd) {
        System.out.println("Controller_listWrite 호출");

        String boardCd_name = "Notice";
        String navBoard = "nav-";
        switch (boardCd) {
            case "20001":
                boardCd_name = "Notice";
                navBoard = navBoard + "notice";
                break;
            case "20002":
                boardCd_name = "Update";
                navBoard = navBoard + "update";
                break;
            case "20003":
                boardCd_name = "Event";
                navBoard = navBoard + "event";
                break;
            case "20004":
                boardCd_name = "Free Board";
                navBoard = navBoard + "free";
                break;
            case "20005":
                boardCd_name = "Sharing information";
                navBoard = navBoard + "information";
                break;
            case "20006":
                boardCd_name = "Share the strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("board_write");
        modelAndView.addObject("board_cd", boardCd);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);

        return modelAndView;
    }

    @RequestMapping("/write_ok")
    public String listWriteOk(@RequestParam(name = "board_cd") String boardCd, HttpServletRequest request,
            Model model) {
        System.out.println("Controller_listWriteOk 호출");
        Post post = new Post();
        Date date = new Date();
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
        }
        post.setPostTitle(request.getParameter("subject"));
        post.setPostTags(request.getParameter("tags"));
        post.setPostContent(request.getParameter("content"));
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
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
            return "redirect:./list?board_cd=" + request.getParameter("board_cd");
        } else {
            // 글쓰기 실패 시 처리
            return "redirect:/error";
        }
    }

    @GetMapping("/modify")
    public ModelAndView listModify(@RequestParam(name = "post_id") int postId,
            @RequestParam("cpage") int cpage) {
        System.out.println("Controller_listModift 호출");

        Post post = new Post();

        post = postInfoRepository.findByPostId(postId);

        String boardCd_name = "Notice";
        String navBoard = "nav-";
        switch (post.getBoardCd()) {
            case "20001":
                boardCd_name = "Notice";
                navBoard = navBoard + "notice";
                break;
            case "20002":
                boardCd_name = "Update";
                navBoard = navBoard + "update";
                break;
            case "20003":
                boardCd_name = "Event";
                navBoard = navBoard + "event";
                break;
            case "20004":
                boardCd_name = "Free Board";
                navBoard = navBoard + "free";
                break;
            case "20005":
                boardCd_name = "Sharing information";
                navBoard = navBoard + "information";
                break;
            case "20006":
                boardCd_name = "Share the strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("board_modify");
        modelAndView.addObject("post", post);
        modelAndView.addObject("cpage", cpage);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);

        return modelAndView;
    }

    @PostMapping("/modify_ok")
    public String listModifyOk(@RequestParam(name = "post_id") int postId,
            @RequestParam(name = "subject") String title,
            @RequestParam(name = "tags") String tags,
            @RequestParam(name = "content") String content,
            @RequestParam("cpage") int cpage) {
        System.out.println("Controller_listModify_ok 호출");
        Date date = new Date();
        try {
            // Find the existing post by ID
            Optional<Post> optionalPost = postInfoRepository.findById(postId);
            Post post = optionalPost.get();
            // Update the post with the new values
            post.setPostTitle(title);
            post.setPostTags(tags);
            post.setPostContent(content);
            post.setUpdatedAt(date);
            
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
                return "redirect:./view?board_cd=" + post.getBoardCd() + "&post_id=" + postId + "&cpage=" + cpage;
            } else {
                // 글쓰기 실패 시 처리
                return "redirect:/error";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/delete")
    public ModelAndView listDelete(@RequestParam(name = "post_id") int postId,
            @RequestParam("cpage") int cpage) {
        System.out.println("Controller_listdelete 호출");

        Post post = new Post();

        post = postInfoRepository.findByPostId(postId);

        String boardCd_name = "Notice";
        String navBoard = "nav-";
        switch (post.getBoardCd()) {
            case "20001":
                boardCd_name = "Notice";
                navBoard = navBoard + "notice";
                break;
            case "20002":
                boardCd_name = "Update";
                navBoard = navBoard + "update";
                break;
            case "20003":
                boardCd_name = "Event";
                navBoard = navBoard + "event";
                break;
            case "20004":
                boardCd_name = "Free Board";
                navBoard = navBoard + "free";
                break;
            case "20005":
                boardCd_name = "Sharing information";
                navBoard = navBoard + "information";
                break;
            case "20006":
                boardCd_name = "Share the strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("board_delete");
        modelAndView.addObject("post", post);
        modelAndView.addObject("cpage", cpage);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);

        return modelAndView;
    }

    @PostMapping("/delete_ok")
    @Transactional
    public String listDeleteOk(@RequestParam(name = "post_id") int postId) {

        System.out.println("Controller_listdeleteOk 호출");

        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);

        System.out.println("댓글 먼저 삭제");
        commentInfoRepository.deleteByPost_PostId(postId);
        System.out.println("게시글 삭제");
        postInfoRepository.deleteById(postId);

        return "redirect:./list?board_cd=" + post.getBoardCd();
    }

    @RequestMapping("/comment_write_ok")
    public String writeComment(@RequestParam("board_cd") int boardCd,
            @RequestParam("post_id") int postId,
            @RequestParam("ccontent") String content,
            @RequestParam("cpage") int cpage) {
        System.out.println("comment_write_ok 호출");
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
        } else {
            System.out.println("로그인을 하세요");
        }
        Date date = new Date();

        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setCommentContent(content);
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setMember(member);

        commentInfoRepository.save(comment);
        // 댓글 카운트 추가
        post.setPostCommentCnt(commentInfoRepository.countByPost_PostId(postId));
        postInfoRepository.save(post);

        // 댓글이 등록된 후에 해당 게시물로 이동
        return "redirect:./view?board_cd=" + boardCd + "&post_id=" + postId + "&cpage=" + cpage;
    }

    @PostMapping("/comment_delete_ok")
    @Transactional
    public String deleteComment(@RequestParam("board_cd") int boardCd,
            @RequestParam("comment_id") int commentId,
            @RequestParam("post_id") int postId,
            @RequestParam("cpage") int cpage) {

        System.out.println("Controller_listdeleteOk 호출");

        System.out.println("댓글 먼저 삭제");
        commentInfoRepository.deleteByCommentGrp(commentId);
        commentInfoRepository.deleteByCommentId(commentId);

        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);
        // 댓글 카운트 수정
        post.setPostCommentCnt(commentInfoRepository.countByPost_PostId(postId));
        postInfoRepository.save(post);

        return "redirect:./view?board_cd=" + boardCd + "&post_id=" + postId + "&cpage=" + cpage;
    }

    @RequestMapping("/reply_comment_write_ok")
    public String writeReplyComment(@RequestParam("board_cd") int boardCd,
            @RequestParam("post_id") int postId,
            @RequestParam("parent_comment_id") int commentId,
            @RequestParam("rcontent") String content,
            @RequestParam("cpage") int cpage) {
        System.out.println("reply_comment_write_ok 호출");
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
        } else {
            System.out.println("로그인을 하세요");
        }
        Date date = new Date();

        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setCommentContent(content);
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setMember(member);
        comment.setCommentGrp(commentId);

        commentInfoRepository.save(comment);
        // 댓글 카운트 추가
        post.setPostCommentCnt(commentInfoRepository.countByPost_PostId(postId));
        postInfoRepository.save(post);

        // 댓글이 등록된 후에 해당 게시물로 이동
        return "redirect:./view?board_cd=" + boardCd + "&post_id=" + postId + "&cpage=" + cpage;
    }

    /* 대댓글 작업하는 부분 */
    // 댓글 목록을 가져오는 메소드
    public ArrayList<CommentTO> getCommentTreeByPostId(int postId) {
        ArrayList<Comment> comments = commentInfoRepository.findByPost_PostId(postId);

        // 댓글 목록을 계층 구조로 가공
        ArrayList<CommentTO> commentTree = buildCommentTree(comments);

        return commentTree;
    }

    // 댓글 목록을 계층 구조로 가공하는 메소드
    private ArrayList<CommentTO> buildCommentTree(ArrayList<Comment> comments) {
        Map<Integer, CommentTO> commentNodeMap = new HashMap<>();

        // 댓글을 CommentNode로 변환하여 Map에 저장
        for (Comment comment : comments) {
            CommentTO commentTO = new CommentTO(comment);
            commentNodeMap.put(comment.getCommentId(), commentTO);
        }

        // 부모-자식 관계를 설정하여 계층 구조 생성
        for (Comment comment : comments) {
            CommentTO commentNode = commentNodeMap.get(comment.getCommentId());

            if (comment.getCommentGrp() != 0) {
                CommentTO parentCommentNode = commentNodeMap.get(comment.getCommentGrp());
                parentCommentNode.getReplies().add(commentNode);
            }
        }

        // 최상위 댓글 노드를 찾아 반환
        ArrayList<CommentTO> rootNodes = new ArrayList<>();
        for (CommentTO commentNode : commentNodeMap.values()) {
            if (commentNode.getComment().getCommentGrp() == 0) {
                rootNodes.add(commentNode);
            }
        }

        return rootNodes;
    }
}