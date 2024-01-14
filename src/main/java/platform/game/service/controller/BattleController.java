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



    @RequestMapping("/view")
    public ModelAndView listView(@RequestParam("id") int postId) {
        ModelAndView mav = new ModelAndView("battle_view");

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint",member.getMemCurPoint());
                mav.addObject("memId",member.getMemId());
            }
        } 
        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);
        

        ArrayList<Comment> comment = commentInfoRepository.findByPost_PostId(postId);
        ArrayList<CommentTO> commentTree = buildCommentTree(comment);


        mav.addObject("post", post);
        mav.addObject("comment", comment);
        mav.addObject("loginCheck", "true");
        mav.addObject("writePost", "true");
        mav.addObject("cpage", 1);
        mav.addObject("board_cd", "00000");
        mav.addObject("commentTree", commentTree);
        mav.addObject("boardCd_name", "boadrCd_name");
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
                mav.addObject("memId",member.getMemId());
                id = member.getMemId();
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
