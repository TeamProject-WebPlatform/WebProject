package platform.game.service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.action.BattleCardAction;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
import platform.game.service.model.TO.BattlePointTO;
import platform.game.service.model.TO.BattleTO;
import platform.game.service.model.TO.CommentTO;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.service.action", "platform.game.service.repository",
        "platform.game.service.model" })
@RequestMapping("/battle")
public class BattleController {

    @Autowired
    BattleCardAction battleCardAction;
    @Autowired
    PostInfoRepository postInfoRepository;
    @Autowired
    CommentInfoRepository commentInfoRepository;
    @Autowired
    MemberInfoRepository memberInfoRepository;


    @RequestMapping("/view")
    public ModelAndView listView(@RequestParam("postId") int postId, @RequestParam("btId") int btId) {
        ModelAndView mav = new ModelAndView("battle_view");
        long id = 0;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint",member.getMemCurPoint());
                id = member.getMemId();
                mav.addObject("memId",id);
            }
        } 
        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);
        Object[] battleTOs = battleCardAction.getBattleTO(id, postId, btId);

        BattleTO bto = (BattleTO)battleTOs[0];
        BattlePointTO pto = (BattlePointTO)battleTOs[1];

        ArrayList<Comment> comment = commentInfoRepository.findByPost_PostId(postId);
        ArrayList<CommentTO> commentTree = buildCommentTree(comment);

        String[][] tmp = bto.getApplicants();
        String[][] applicants = new String[tmp.length][5];
        for(int i =0;i<tmp.length;i++){
            String[] s = tmp[i];
            Member member = memberInfoRepository.findById(Long.parseLong(s[0])).isPresent() ? memberInfoRepository.findById(Long.parseLong(s[0])).get() : null;
            applicants[i][0] = s[0]; // memId
            applicants[i][1] = s[1]; // 보류 상태
            applicants[i][2] = s[2]; // 신청 시간
            applicants[i][3] = member.getMemNick(); // 닉네임
            applicants[i][4] = String.valueOf(member.getMemLvl()); // 레벨
        }
        System.out.println(post.getBoardCd());
        mav.addObject("bto",bto);
        mav.addObject("pto",pto);
        mav.addObject("post", post);
        mav.addObject("applicants", applicants);
        mav.addObject("comment", comment);
        mav.addObject("board_cd", "00000");
        mav.addObject("commentTree", commentTree);
        mav.addObject("boardCd_name", "Battle");
        mav.addObject("navBoard", "nav-battle");
        return mav;
    }

    @RequestMapping("")
    public ModelAndView battle(){
        long id = 0;
        ModelAndView mav = new ModelAndView("battle");
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint",member.getMemCurPoint());
                id = member.getMemId();
                mav.addObject("memId",id);
            }
        } else {
        }
        List[] battleList = battleCardAction.getBattleList(id);
        List<BattleTO> battleTOList = battleList[0];
        List<BattlePointTO> battlePointTOList = battleList[1];
        
        mav.addObject("battleTOList",battleTOList);
        mav.addObject("battlePointTOList",battlePointTOList);

        return mav;
    }


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
