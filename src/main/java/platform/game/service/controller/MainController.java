package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.transaction.Transactional;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
import platform.game.service.mapper.SqlMapperInter;
import platform.game.service.model.DAO.RankDAO;
import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.model.TO.PointRankTO;
import platform.game.service.model.TO.RollingRankTO;
import platform.game.service.model.TO.WinRankTO;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.service.MemberInfoDetails;

// Spring Security의 /login 페이지 안되게
@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.security" })
public class MainController {
    @Autowired
    SqlMapperInter sqlMapperInter;

    @Autowired
    RankDAO rankDAO;

    private List<RollingRankTO> rollingRankList;

    public MainController(SqlMapperInter sqlMapperInter) {
        this.sqlMapperInter = sqlMapperInter;
        this.rollingRankList = sqlMapperInter.getRol(); // 초기화
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void RollingTimer() {
        rollingRankList = sqlMapperInter.getRol();
    }

    @Autowired
    private PostInfoRepository postInfoRepository;

    @Autowired
    private CommentInfoRepository commentInfoRepository;
    @RequestMapping({ "/", "/home" })
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("03_home");
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
            }
        } else {
            System.out.println("멤버 없음");
        }
        return mav;

    }

    @PostMapping("/roll") // 메인화면 Rolling RandomList
    @ResponseBody
    public List<RollingRankTO> roll() {
        List<RollingRankTO> list = rollingRankList;
        return list;
    }

    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("list");
    }

    @GetMapping("/show")
    public ModelAndView show() {
        return new ModelAndView("shop");
    }

    @GetMapping("/rank")
    public ModelAndView rank(ModelAndView modelAndView) {
        List<WinRankTO> WinRanklists = sqlMapperInter.getWinrank();
        List<LevelRankTO> LevelRanklists = sqlMapperInter.getLevelrank();
        List<PointRankTO> PointRanklists = sqlMapperInter.getPointrank();

        List<Integer> WinRanks = rankDAO.getWinList();
        List<Integer> LevelLists = rankDAO.getLevelList();
        List<Integer> PointRanks = rankDAO.getPointList();

        modelAndView.setViewName("rank");
        modelAndView.addObject("winlist", WinRanklists);
        modelAndView.addObject("levellist", LevelRanklists);
        modelAndView.addObject("pointlist", PointRanklists);

        modelAndView.addObject("levels", LevelLists);
        modelAndView.addObject("winrank", WinRanks);
        modelAndView.addObject("pointrank", PointRanks);

        return modelAndView;
    }

    @GetMapping("/getMainFragment")
    public String getMainFragment(Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환

        return "fragments/content/main";
    }

    @GetMapping("/getBoardListFragment")
    public String getBoardListFragment(@RequestParam("board_cd") String boardCd, Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardListFragment 호출");

        String loginCheck = "false";

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            System.out.println("멤버 있음 ");
            loginCheck = "true";
        } else {
            System.out.println("멤버 없음");
            loginCheck = "false";
        }

        ArrayList<Post> lists = postInfoRepository.findByBoardCdOrderByPostIdDesc(boardCd);

        for (Post postComment : lists) {
            System.out.println("PostId: " + postComment.getPostId());
            System.out.println("post_comment : " + commentInfoRepository.countByPost_PostId(postComment.getPostId()));
        }

        // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
        System.out.println("list : " + boardCd);
        int RboardCd = Integer.parseInt(boardCd.replaceAll("\"", ""));
        System.out.println("list1 : " + RboardCd);
        model.addAttribute("boardCd", RboardCd);

        model.addAttribute("lists", lists);
        // model.addAttribute("boardCd", boardCd);
        model.addAttribute("loginCheck", loginCheck);

        return "fragments/content/board/list";
    }

    @GetMapping("/getBoardViewFragment")
    public String getBoardViewFragment(@RequestParam(name = "post_id") int postId, Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardViewFragment 호출");

        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);

        Member member = null;
        String loginCheck = "true";

        // 멤버 비교 변수들
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

        // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
        System.out.println("view : " + post.getBoardCd());
        int RboardCd = Integer.parseInt(post.getBoardCd().replaceAll("\"", ""));
        System.out.println("view1 : " + RboardCd);
        model.addAttribute("boardCd", RboardCd);

        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        model.addAttribute("loginCheck", loginCheck);
        model.addAttribute("writePost", writePost);

        return "fragments/content/board/view";
    }

    @GetMapping("/getBoardWriteFragment")
    public String getBoardLWriteFragment(@RequestParam("board_cd") String boardCd, Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardWriteFragment 호출");

        // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
        System.out.println("write : " + boardCd);
        int RboardCd = Integer.parseInt(boardCd.replaceAll("\"", ""));
        System.out.println("write1 : " + RboardCd);

        model.addAttribute("boardCd", RboardCd);

        return "fragments/content/board/write";
    }

    @GetMapping("/getBoardWrite_okFragment")
    public String getBoardLWrite_okFragment(@RequestParam("board_cd") String boardCd,
            @RequestParam("Wboard_cd") String WboardCd,
            @RequestParam("subject") String title,
            @RequestParam("tags") String tags,
            @RequestParam("content") String content,
            Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardWrite_okFragment 호출");

        Post post = new Post();
        Date date = new Date();
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
        }
        post.setPostTitle(title);
        post.setPostTags(tags);
        post.setPostContent(content);
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        post.setBoardCd(WboardCd);
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
            // 성공하면 뷰로 이동
            // 작성한 글로 이동하기 위한 정보 가져오기
            post = postInfoRepository.findByPostId(post.getPostId());

            // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
            System.out.println("write_ok : " + post.getBoardCd());
            int RboardCd = Integer.parseInt(post.getBoardCd().replaceAll("\"", ""));
            System.out.println("write_ok1 : " + RboardCd);
            model.addAttribute("boardCd", RboardCd);

            model.addAttribute("post", post);

            return "fragments/content/board/view";
        } else {
            // 글쓰기 실패 시 처리
            return "redirect:/error";
        }

    }

    @GetMapping("/getBoardModifyFragment")
    public String getBoardLModifyFragment(@RequestParam("post_id") int postId, Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardModifyFragment 호출");

        Post post = new Post();

        post = postInfoRepository.findByPostId(postId);

        // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
        System.out.println("modify : " + post.getBoardCd());
        int RboardCd = Integer.parseInt(post.getBoardCd().replaceAll("\"", ""));
        System.out.println("modify1 : " + RboardCd);

        model.addAttribute("post", post);
        model.addAttribute("boardCd", RboardCd);

        return "fragments/content/board/modify";
    }

    @GetMapping("/getBoardModify_okFragment")
    public String getBoardLModify_okFragment(@RequestParam("post_id") int postId,
            @RequestParam("subject") String title,
            @RequestParam("tags") String tags,
            @RequestParam("content") String content,
            Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardModify_okFragment 호출");
        Date date = new Date();

        // Find the existing post by ID
        Optional<Post> optionalPost = postInfoRepository.findById(postId);

        Post post = optionalPost.get();
        // Update the post with the new values
        post.setPostTitle(title);
        post.setPostTags(tags);
        post.setPostContent(content);
        post.setUpdatedAt(date);

        // Save the updated post
        int flag = 1;
        try {
            postInfoRepository.save(post);
            flag = 0;
        } catch (Exception e) {
            System.out.println("수정 오류 : " + e.getMessage());
            flag = 1;
        }

        if (flag == 0) {
            // 성공하면 뷰로 이동
            // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
            System.out.println("modify_ok : " + post.getBoardCd());
            int RboardCd = Integer.parseInt(post.getBoardCd().replaceAll("\"", ""));
            System.out.println("modify_ok1 : " + RboardCd);
            model.addAttribute("boardCd", RboardCd);

            model.addAttribute("post", post);

            return "fragments/content/board/view";
        } else {
            // 글쓰기 실패 시 처리
            return "redirect:/error";
        }

    }

    @GetMapping("/getBoardDeleteFragment")
    public String getBoardLDeleteFragment(@RequestParam("post_id") int postId, Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardDeleteFragment 호출");

        Post post = new Post();

        post = postInfoRepository.findByPostId(postId);

        // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
        System.out.println("delete : " + post.getBoardCd());
        int RboardCd = Integer.parseInt(post.getBoardCd().replaceAll("\"", ""));
        System.out.println("delete1 : " + RboardCd);

        model.addAttribute("post", post);
        model.addAttribute("boardCd", RboardCd);

        return "fragments/content/board/delete";
    }

    @GetMapping("/getBoardDelete_okFragment")
    public String getBoardLDelete_okFragment(@RequestParam("board_cd") String boardCd,
            @RequestParam("post_id") int postId, Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardDelete_okFragment 호출");

        int flag = 1;
        try {
            // 댓글 먼저 삭제
            commentInfoRepository.deleteByPost_PostId(postId);
            postInfoRepository.deleteById(postId);
            flag = 0;
        } catch (Exception e) {
            System.out.println("수정 오류 : " + e.getMessage());
            flag = 1;
        }

        System.out.println("flag : " + flag);

        if (flag == 0) {
            // 성공하면 리스트로 이동
            ArrayList<Post> lists = postInfoRepository.findByBoardCdOrderByPostIdDesc(boardCd);

            // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
            System.out.println("delete_ok : " + boardCd);
            int RboardCd = Integer.parseInt(boardCd.replaceAll("\"", ""));
            System.out.println("delete_ok1 : " + RboardCd);
            model.addAttribute("boardCd", RboardCd);

            model.addAttribute("lists", lists);

            return "fragments/content/board/list";
        } else {
            // 글쓰기 실패 시 처리
            return "redirect:/error";
        }

    }

    @GetMapping("/getBoardComment_okFragment")
    public String getBoardLComment_okFragment(@RequestParam("post_id") int postId,
            @RequestParam("content") String content,
            Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardComment_okFragment 호출");

        Post post = new Post();
        Date date = new Date();
        Member member = null;
        Comment comment = new Comment();

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
        } else {
            System.out.println("로그인을 하세요");
        }

        post = postInfoRepository.findByPostId(postId);

        comment.setPost(post);
        comment.setCommentContent(content);
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setMember(member);

        // 멤버 비교 변수들
        long id = 1; // 멤버 비교를 위한 변수
        long pid = post.getMember().getMemId(); // 멤버 비교를 위한 게시물 변수
        String writePost = "false"; // 결과 보내주는 변수
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
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

        int flag = 1;
        try {
            commentInfoRepository.save(comment);
            // 댓글 카운트 저장
            post.setPostCommentCnt(commentInfoRepository.countByPost_PostId(postId));
            postInfoRepository.save(post);

            flag = 0;
        } catch (Exception e) {
            System.out.println("WriteOk(Post post) 오류 : " + e.getMessage());
            flag = 1;
        }

        if (flag == 0) {
            // 성공하면 뷰로 이동
            post = postInfoRepository.findByPostId(post.getPostId());

            ArrayList<Comment> comments = commentInfoRepository.findByPost_PostId(postId);

            // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
            System.out.println("comment_ok : " + post.getBoardCd());
            int RboardCd = Integer.parseInt(post.getBoardCd().replaceAll("\"", ""));
            System.out.println("comment_ok1 : " + RboardCd);
            model.addAttribute("boardCd", RboardCd);

            model.addAttribute("post", post);
            model.addAttribute("comment", comments);
            model.addAttribute("writePost", writePost);

            return "fragments/content/board/view";
        } else {
            // 글쓰기 실패 시 처리
            return "redirect:/error";
        }

    }

    @GetMapping("/getBoardCommentDelete_okFragment")
    @Transactional
    public String getBoardCommentDelete_okFragment(@RequestParam("post_id") int postId,
            @RequestParam("comment_id") int commentId, Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardCommentDelete_okFragment 호출");

        System.out.println("commentId : " + commentId);

        Post post = new Post();

        int flag = 1;
        try {
            commentInfoRepository.deleteByCommentId(commentId);

            // 댓글 카운트 저장
            post.setPostCommentCnt(commentInfoRepository.countByPost_PostId(postId));
            postInfoRepository.save(post);

            System.out.println("CommentCnt : " + post.getPostCommentCnt());

            flag = 0;
        } catch (Exception e) {
            System.out.println("수정 오류 : " + e.getMessage());
            flag = 1;
        }

        if (flag == 0) {
            // 성공하면 뷰로 이동
            post = postInfoRepository.findByPostId(postId);

            ArrayList<Comment> comment = commentInfoRepository.findByPost_PostId(postId);
            // 멤버 비교 변수들
            long id = 1; // 멤버 비교를 위한 변수
            long pid = post.getMember().getMemId(); // 멤버 비교를 위한 게시물 변수
            String writePost = "false"; // 결과 보내주는 변수
            Member member = null;

            if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getMember();
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

            // 쌍따옴표로 넘어와서 쌍따옴표 없에는 문구(원인 찾으면 없에도 됨)
            System.out.println("comment_delete_ok : " + post.getBoardCd());
            int RboardCd = Integer.parseInt(post.getBoardCd().replaceAll("\"", ""));
            System.out.println("comment_delete_ok1 : " + RboardCd);
            model.addAttribute("boardCd", RboardCd);

            model.addAttribute("post", post);
            model.addAttribute("comment", comment);
            model.addAttribute("writePost", writePost);

            return "fragments/content/board/view";
        } else {
            // 글쓰기 실패 시 처리
            return "redirect:/error";
        }

    }
}