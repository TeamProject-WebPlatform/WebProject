package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.transaction.Transactional;
import platform.game.service.action.BattleCardAction;
import platform.game.service.action.LoginAction;
import platform.game.service.entity.Comment;
import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberProfile;
import platform.game.service.entity.Post;
import platform.game.service.mapper.SqlMapperInter;
import platform.game.service.model.DAO.RankDAO;
import platform.game.service.model.TO.BattlePointTO;
import platform.game.service.model.TO.BattleTO;
import platform.game.service.model.TO.BoardCpageTO;
import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.model.TO.PointRankTO;
import platform.game.service.model.TO.RollingRankTO;
import platform.game.service.model.TO.SwiperProfileTO;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberFavoriteGameRepository;
import platform.game.service.repository.MemberProfileRepository;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.repository.SigninHistoryRepository;
import platform.game.service.repository.UpdatePointHistory;
import platform.game.service.service.MemberInfoDetails;
import platform.game.service.service.SigninHistoryService;

// Spring Security의 /login 페이지 안되게
@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.security" })
public class MainController {

    @Autowired
    SqlMapperInter sqlMapperInter;

    @Autowired
    SigninHistoryService signinHistoryService;

    @Autowired
    BattleCardAction battleCardAction;

    @Autowired
    RankDAO rankDAO;

    @Autowired
    @Qualifier("updatePointHistoryImpl")
    UpdatePointHistory updatePointHistory;

    @Autowired
    SigninHistoryRepository signinHistoryRepository;

    @Autowired
    MemberFavoriteGameRepository memberFavoriteGameRepository;

    @Autowired
    MemberProfileRepository memberProfileRepository;

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

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @RequestMapping("/t/{no}")
    public String template(@PathVariable("no") String no) {

        return "template/" + no;
    }

    @RequestMapping("/loadertest")
    public String loader() {
        return "loadertest";
    }

    @RequestMapping({ "/", "/home" })
    public ModelAndView main() {
        String boardCd_notice = "20001";
        String boardCd_update = "20002";
        String boardCd_event = "20003";
        String boardCd_free = "20004";
        long id = 0;

        ArrayList<Post> notice_lists = postInfoRepository.findTop10ByBoardCdOrderByCreatedAtDesc(boardCd_notice);
        ArrayList<Post> update_lists = postInfoRepository.findTop10ByBoardCdOrderByCreatedAtDesc(boardCd_update);
        ArrayList<Post> event_lists = postInfoRepository.findTop10ByBoardCdOrderByCreatedAtDesc(boardCd_event);
        ArrayList<Post> free_lists = postInfoRepository.findTop10ByBoardCdOrderByCreatedAtDesc(boardCd_free);
        List<LevelRankTO> getLevelTable = sqlMapperInter.getTop10LevelRanks();

        ModelAndView mav = new ModelAndView("index");
        BoardCpageTO cpageTO = new BoardCpageTO();
        
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getMember();

            
            if (member != null) {
                LoginAction loginAction = new LoginAction(signinHistoryService, updatePointHistory, signinHistoryRepository);  
                Object[] o = loginAction.getLogin(member);
                boolean isFirstLogin = (Boolean)o[0];

                MemberProfile memberProfile = memberProfileRepository.findProfileIntroByMemId(member.getMemId());
                if(isFirstLogin){ 
                    mav.addObject("currentPoint", o[1]);
                }else{ 
                    mav.addObject("currentPoint", member.getMemCurPoint());
                }
                mav.addObject("isFirstLogin", isFirstLogin);
                mav.addObject("nickname", member.getMemNick());
                id = member.getMemId();
                mav.addObject("memId", member.getMemId());
                mav.addObject("memberProfile",memberProfile);
                
                System.out.println("isFirstLogin: " + isFirstLogin);
            }
        } else {
            System.out.println("멤버 없음");
        }

        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        System.out.println("총 방문자 수1: " + visitCount.getRemark1());
        System.out.println("어제 방문자 수1: " + visitCount.getRemark2());
        System.out.println("오늘 방문자 수1: " + visitCount.getRemark3());
        // 사이트 접속하면 방문자수 카운트해서 저장하기
        int total = Integer.parseInt(visitCount.getRemark1()) + 1;
        int today = Integer.parseInt(visitCount.getRemark3()) + 1;
        commonCodeRepository.updateRemarksByCd(String.valueOf(total), String.valueOf(today));

        mav.addObject("totalCount", total);
        mav.addObject("todayCount", today);
        List[] battleList = battleCardAction.getBattleList(id);
        List<BattleTO> battleTOList = battleList[0];
        List<BattlePointTO> battlePointTOList = battleList[1];

        // 필요한 개수만큼 데이터 추출
        int limit = Math.min(battleTOList.size(), 4);
        List<BattleTO> limitedBattleTOList = battleTOList.subList(0, limit);

        // battleTO에 있는 호스트와 클라이언트 유저들 프로필 가져오기
        List<String> battleTOHostProfileImage = new ArrayList<String>();

        List<String> battleTOClientProfileImage = new ArrayList<String>();

        for(int i=0; i<limitedBattleTOList.size();i++){
            BattleTO to = limitedBattleTOList.get(i);

            String HostProfileImage = memberProfileRepository.BattleProfileImage(to.getHostNick());

            battleTOHostProfileImage.add(HostProfileImage);

            if (to.getClientNick()!=null){
                String ClientProfileImage = memberProfileRepository.BattleProfileImage(to.getClientNick());
                battleTOClientProfileImage.add(ClientProfileImage);

            } else {
                String ClientProfileImage = null;
                battleTOClientProfileImage.add(ClientProfileImage);
            }
        }

        mav.addObject("notice_lists", notice_lists);
        mav.addObject("update_lists", update_lists);
        mav.addObject("event_lists", event_lists);
        mav.addObject("free_lists", free_lists);
        mav.addObject("boardCd", boardCd_notice);
        mav.addObject("cpage", cpageTO);
        mav.addObject("level", getLevelTable);
        mav.addObject("battleTOList", limitedBattleTOList);
        mav.addObject("battlePointTOList", battlePointTOList);
        mav.addObject("HostProfileImage", battleTOHostProfileImage);
        mav.addObject("ClientProfileImage", battleTOClientProfileImage);
        return mav;
    }

    @PostMapping("/roll") // 메인화면 Rolling RandomList
    @ResponseBody
    public List<RollingRankTO> roll() {
        List<RollingRankTO> list = new ArrayList<RollingRankTO>();
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                if (!memberFavoriteGameRepository.existsByMemId(member.getMemId())) {
                    list = rollingRankList;
                } else {
                    List<RollingRankTO> first = sqlMapperInter
                            .getOtherRol(memberFavoriteGameRepository.getGameCd(member.getMemId(), 1));
                    List<RollingRankTO> second = sqlMapperInter
                            .getOtherRol(memberFavoriteGameRepository.getGameCd(member.getMemId(), 2));
                    List<RollingRankTO> third = sqlMapperInter
                            .getOtherRol(memberFavoriteGameRepository.getGameCd(member.getMemId(), 3));
                    List<RollingRankTO> other = sqlMapperInter.getOtherRol("0");
                    for (int i = 0; i < first.size(); i++)
                        list.add(first.get(i));
                    for (int i = 0; i < second.size(); i++)
                        list.add(second.get(i));
                    for (int i = 0; i < third.size(); i++)
                        list.add(third.get(i));
                    for (int i = 0; i < other.size(); i++)
                        list.add(other.get(i));
                }
            } else {
                list = rollingRankList;
            }
        } else {
            list = rollingRankList;
        }
        return list;
    }

    @PostMapping("/userprofile")
    @ResponseBody
    public List<SwiperProfileTO> userProfile(@RequestBody List<RollingRankTO> SwiperData) {
        List<SwiperProfileTO> list = new ArrayList<SwiperProfileTO>();
        for (int i = 0; i < 16; i++) {
            SwiperProfileTO to = sqlMapperInter.SwiperProfile(SwiperData.get(i).getMem_id());
            list.add(to);
        }
        return list;
    }

    // @GetMapping("/list")
    // public ModelAndView list() {
    // return new ModelAndView("list");
    // }

    @GetMapping("/show")
    public ModelAndView show() {
        return new ModelAndView("shop");
    }

    @GetMapping("/battle_view")
    public ModelAndView battleView() {
        return new ModelAndView("battle_view");
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