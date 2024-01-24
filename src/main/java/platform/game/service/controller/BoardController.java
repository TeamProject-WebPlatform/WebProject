package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.security.core.Authentication;

import org.springframework.security.access.prepost.PreAuthorize;

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
import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberProfile;
import platform.game.service.entity.Post;
import platform.game.service.model.TO.BoardCpageTO;
import platform.game.service.model.TO.CommentTO;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.repository.UpdatePointHistory;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberProfileRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.service.repository",
        "platform.game.service.model" })
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private PostInfoRepository postInfoRepository;

    @Autowired
    private CommentInfoRepository commentInfoRepository;

    @Autowired
    @Qualifier("updatePointHistoryImpl")
    private UpdatePointHistory updatePointHistory;

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Autowired
    private MemberProfileRepository profileRepository;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam("board_cd") String boardCd, HttpServletRequest request) {
        ArrayList<Post> lists = postInfoRepository.findByBoardCdOrderByCreatedAtDesc(boardCd);

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
                boardCd_name = "Free";
                navBoard = navBoard + "free";
                break;
            case "20005":
                boardCd_name = "Share";
                navBoard = navBoard + "share";
                break;
            case "20006":
                boardCd_name = "Strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        ModelAndView modelAndView = new ModelAndView();

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            System.out.println("멤버 있음 ");
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                modelAndView.addObject("nickname", member.getMemNick());
                modelAndView.addObject("level", member.getMemLvl());
                modelAndView.addObject("currentPoint", member.getMemCurPoint());
                modelAndView.addObject("memId", member.getMemId());
                modelAndView.addObject("memberProfile", memberProfile);
            }
        } else {
            System.out.println("멤버 없음");
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

        modelAndView.setViewName("board_list");
        modelAndView.addObject("lists", lists);
        modelAndView.addObject("boardCd", boardCd);
        modelAndView.addObject("cpage", cpageTO);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);

        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());

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
                boardCd_name = "free";
                navBoard = navBoard + "free";
                break;
            case 20005:
                boardCd_name = "information";
                navBoard = navBoard + "information";
                break;
            case 20006:
                boardCd_name = "strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);

        Member member = null;
        long id = 1; // 멤버 비교를 위한 변수
        long pid = post.getMember().getMemId(); // 멤버 비교를 위한 게시물 변수
        String writePost = "false"; // 결과 보내주는 변수

        // hit 증가시키기
        post.setPostHit(post.getPostHit() + 1);
        postInfoRepository.save(post);

        ModelAndView modelAndView = new ModelAndView();
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                modelAndView.addObject("nickname", member.getMemNick());
                modelAndView.addObject("level", member.getMemLvl());
                modelAndView.addObject("currentPoint", member.getMemCurPoint());
                modelAndView.addObject("memId", member.getMemId());
                modelAndView.addObject("memberProfile", memberProfile);
            }
            id = member.getMemId();
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

        modelAndView.setViewName("board_view");
        modelAndView.addObject("post", post);
        modelAndView.addObject("comment", comment);
        modelAndView.addObject("writePost", writePost);
        modelAndView.addObject("cpage", cpage);
        modelAndView.addObject("board_cd", boardCd);
        modelAndView.addObject("commentTree", commentTree);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);
        modelAndView.addObject("post", post);
        modelAndView.addObject("commentTree", getCommentTreeByPostId(postId));
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());

        return modelAndView;
    }

    @RequestMapping("/write")
    @PreAuthorize("hasAuthority('ROLE_USER')")
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
                boardCd_name = "free";
                navBoard = navBoard + "free";
                break;
            case "20005":
                boardCd_name = "information";
                navBoard = navBoard + "information";
                break;
            case "20006":
                boardCd_name = "strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        ModelAndView modelAndView = new ModelAndView();

        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        if (member != null) {
            MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
            modelAndView.addObject("nickname", member.getMemNick());
            modelAndView.addObject("level", member.getMemLvl());
            modelAndView.addObject("currentPoint", member.getMemCurPoint());
            modelAndView.addObject("memId", member.getMemId());
            modelAndView.addObject("memberProfile", memberProfile);
        }
        modelAndView.setViewName("board_write");
        modelAndView.addObject("board_cd", boardCd);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());

        return modelAndView;
    }

    @Transactional
    @RequestMapping("/write_ok")
    public String listWriteOk(@RequestParam(name = "board_cd") String boardCd, HttpServletRequest request,
            Model model) {
        System.out.println("Controller_listWriteOk 호출");

        int flag = 1;

        try {
            // 게시글 목록을 가져오는 로직
            ArrayList<Post> posts = postInfoRepository.findByBoardCdOrderByPostIdDesc(boardCd);

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

            postInfoRepository.save(post);
            // 변경: 첫 번째 글 작성 시에는 특정 포인트를 주기
            if (isFirstPost(posts)) {
                int firstPostPoint = 100; // 첫 번째 글 작성 시 부여할 포인트
                updatePointHistory.insertPointHistoryByMemId(post.getMember().getMemId(), "50203", firstPostPoint);
                flag = 0;
                System.out.println("첫 번째 글 작성 포인트 지급");
                int point = 1;
                return "redirect:./point_ok?board_cd=" + request.getParameter("board_cd") + "&point=" + point;
            } else if (isMultipleOfFivePosts(posts)) {
                int additionalPostPoint = 50;
                updatePointHistory.insertPointHistoryByMemId(member.getMemId(), "50204", additionalPostPoint);
                flag = 0;

                System.out.println("5개의 글 작성 포인트 지급");
                int point = 2;

                return "redirect:./point_ok?board_cd=" + request.getParameter("board_cd") + "&point=" + point;

            } else {
                flag = 0;
                System.out.println("포인트 지급 없음");
            }

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
    @PreAuthorize("hasAuthority('ROLE_USER')")
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
                boardCd_name = "free";
                navBoard = navBoard + "free";
                break;
            case "20005":
                boardCd_name = "information";
                navBoard = navBoard + "information";
                break;
            case "20006":
                boardCd_name = "strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        ModelAndView modelAndView = new ModelAndView();

        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        if (member != null) {
            MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
            modelAndView.addObject("nickname", member.getMemNick());
            modelAndView.addObject("level", member.getMemLvl());
            modelAndView.addObject("currentPoint", member.getMemCurPoint());
            modelAndView.addObject("memId", member.getMemId());
            modelAndView.addObject("memberProfile", memberProfile);
        }
        modelAndView.setViewName("board_modify");
        modelAndView.addObject("post", post);
        modelAndView.addObject("cpage", cpage);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());

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
    @PreAuthorize("hasAuthority('ROLE_USER')")
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
                boardCd_name = "free";
                navBoard = navBoard + "free";
                break;
            case "20005":
                boardCd_name = "information";
                navBoard = navBoard + "information";
                break;
            case "20006":
                boardCd_name = "strategy";
                navBoard = navBoard + "strategy";
                break;

            default:
                break;
        }

        ModelAndView modelAndView = new ModelAndView();
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        if (member != null) {
            MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
            modelAndView.addObject("nickname", member.getMemNick());
            modelAndView.addObject("level", member.getMemLvl());
            modelAndView.addObject("currentPoint", member.getMemCurPoint());
            modelAndView.addObject("memId", member.getMemId());
            modelAndView.addObject("memberProfile", memberProfile);
        }
        modelAndView.setViewName("board_delete");
        modelAndView.addObject("post", post);
        modelAndView.addObject("cpage", cpage);
        modelAndView.addObject("boardCd_name", boardCd_name);
        modelAndView.addObject("navBoard", navBoard);
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());

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
    @PreAuthorize("hasAuthority('ROLE_USER')")
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

        // 댓글 작성 후 포인트 지급
        List<Comment> userComments = commentInfoRepository.findByMember_MemId(member.getMemId());
        if (isFirstComment(userComments)) {
            int commentPoint = 100; // 첫 댓글 작성 시 부여할 포인트
            updatePointHistory.insertPointHistoryByMemId(member.getMemId(), "50205", commentPoint);
            System.out.println("첫 댓글 작성 포인트 지급");
            int point = 1;
            return "redirect:./point_ok?board_cd=" + boardCd + "&point=" + point;
        } else if (isMultipleOfFiveComments(userComments)) {
            int commentPoint = 50; // 5개 단위 댓글 작성 시 부여할 포인트
            updatePointHistory.insertPointHistoryByMemId(member.getMemId(), "50206", commentPoint);
            System.out.println("5개의 배수로 댓글 작성 포인트 지급");
            int point = 1;
            return "redirect:./point_ok?board_cd=" + boardCd + "&point=" + point;
        }

        // 댓글 카운트 추가
        post.setPostCommentCnt(commentInfoRepository.countByPost_PostId(postId));
        postInfoRepository.save(post);

        // 댓글이 등록된 후에 해당 게시물로 이동
        return "redirect:./view?board_cd=" + boardCd + "&post_id=" + postId + "&cpage=" + cpage;
    }

    @PostMapping("/comment_delete_ok")
    @PreAuthorize("hasAuthority('ROLE_USER')")
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
    @PreAuthorize("hasAuthority('ROLE_USER')")
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

    // 변경: 특정 사용자가 작성한 글의 개수 조회 (인스턴스 메서드로 변경)

    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof MemberInfoDetails) {
            return ((MemberInfoDetails) authentication.getPrincipal()).getMember();
        }
        return null; // 혹은 예외 처리 등을 추가할 수 있습니다.
    }

    public int getPostCountByMember(List<Post> posts) {
        int count = 1;
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        for (Post p : posts) {
            if (p.getMember() != null && p.getMember().getMemId() == member.getMemId()) {
                count++;
            }
        }
        System.out.println("사용자 " + member.getMemId() + "의 글 개수: " + count);
        System.out.println("현재 게시글 작성자: " + member.getMemId());
        // System.out.println("현재 글의 boardCd: " + member.boardCd());
        return count;
    }

    // 변경: 첫 번째 글 작성 여부 확인 (인스턴스 메서드로 변경)
    public boolean isFirstPost(List<Post> posts) {
        return getPostCountByMember(posts) == 1;
    }

    // 변경: 5개 단위로 작성 여부 확인 (인스턴스 메서드로 변경)
    public boolean isMultipleOfFivePosts(List<Post> posts) {
        int postCount = getPostCountByMember(posts);
        return postCount > 0 && (postCount % 5 == 0);
    }

    // 뎃글
    public int getCommentCountByMember(List<Comment> comments) {
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        if (member == null) {
            return 0;
        }
        int commentCount = (int) comments.stream()
                .filter(comment -> comment.getMember() != null && comment.getMember().getMemId() == member.getMemId())
                .count();
        System.out.println("현재 게시글 작성자: " + member.getMemId());
        System.out.println("댓글 개수: " + commentCount);

        return commentCount;
    }

    public boolean isFirstComment(List<Comment> comments) {
        return getCommentCountByMember(comments) == 1;
    }

    public boolean isMultipleOfFiveComments(List<Comment> comments) {
        int commentCount = getCommentCountByMember(comments);
        return commentCount > 0 && (commentCount % 5 == 0);
    }

    // 포인트 지급 관련 메세지 전송
    @GetMapping("/point_ok")
    public ModelAndView PointOk(@RequestParam("board_cd") int boardCd, @RequestParam("point") int point) {
        System.out.println("point_ok 호출");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("point_ok");
        modelAndView.addObject("boardCd", boardCd);
        modelAndView.addObject("point", point);

        return modelAndView;
    }

}