package platform.game.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView main(ModelAndView modelAndView) {
        ModelAndView mav = new ModelAndView("index");
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
            }
        } else {
            System.out.println("멤버 없음");
        }

        modelAndView.setViewName("index");
        modelAndView.addObject("rol", rollingRankList);

        return modelAndView;

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
        // model.addAttribute("test","굿");

        return "fragments/content/main";
    }

    @GetMapping("/getBoardListFragment")
    public String getBoardListFragment(@RequestParam("board_cd") String boardCd, Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        ArrayList<Post> lists = postInfoRepository.findByBoardCdOrderByPostIdDesc(boardCd);

        model.addAttribute("lists", lists);
        model.addAttribute("boardCd", boardCd);

        return "fragments/content/board/list";
    }

    @GetMapping("/getBoardViewFragment")
    public String getBoardViewFragment(@RequestParam(name = "post_id") int postId,
            @RequestParam("board_cd") String boardCd,
            Model model) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        Post post = new Post();

        post = postInfoRepository.findByPostId(postId);

        // 댓글 불러오기
        ArrayList<Comment> commentList = commentInfoRepository.findByPost_PostId(postId);

        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);

        return "fragments/content/board/list";
    }
    /*
     * Board는 컨트롤러 html 수정된거 오면 복붙해서 넣기로
     * 
     */
}